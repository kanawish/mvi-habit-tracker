package com.kanastruk.sample.ios

import co.touchlab.kermit.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

/**
 * Following examples from
 * https://johnoreilly.dev/posts/kotlinmultiplatform-swift-combine_publisher-flow/
 * also.. https://betterprogramming.pub/using-kotlin-flow-in-swift-3e7b53f559b6
 *
 * Also taking up some of my JS stuff, again, hoping to modify all that.
 *
 * TODO: In final approach, DI provides scopes, etc.
 */
class MockModel:CoroutineScope by MainScope() {
    val log = Logger.withTag("🥸")

    private val _store = MutableStateFlow(42)
    val store: StateFlow<Int> = _store.asStateFlow()

    init {
        log.d("MockModel.init { }")

        val delayFlow = flow {
            var count = 0
            while (count++ < 60*60) { // 'hour'
                log.d("flow { emit(1) }")
                emit(1)
                kotlinx.coroutines.delay(1000)
            }
        }

        log.d("preparing to launch")
        launch {
            delayFlow.collect {
                log.d("delayFlow.collect($it)")
                _store.value = _store.value + 1
            }
        }
    }

    /**
     * TODO: Try just calling cancel on Job from iOS?
     */
    fun subscribe(collector:(Int)->Unit):Job {
        return launch { store.collect { collector(it) } }
    }

    /**
     * Reset counter to 42.
     */
    fun processReset() {
        _store.value = 42
    }
}

@Suppress("Unused", "MemberVisibilityCanBePrivate") // Members are called from Swift
class AdaptedMockModel(val model: MockModel) {
    val store:FlowAdapter<Int> = FlowAdapter(model,model.store)
}
