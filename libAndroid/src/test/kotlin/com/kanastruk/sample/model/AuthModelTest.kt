@file:OptIn(ExperimentalCoroutinesApi::class)

package com.kanastruk.sample.model

import com.kanastruk.sample.common.rest.auth.Credentials
import com.kanastruk.sample.common.model.AuthState.*
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import timber.log.Timber
import kotlin.test.assertIs

/**
 * Reference material:
 *
 * http://d.android.com/tools/testing
 * https://proandroiddev.com/testing-retrofit-converter-with-mock-webserver-50f3e1f54013
 * https://medium.com/mobile-app-development-publication/kotlin-coroutine-scope-context-and-job-made-simple-5adf89fcfe94
 * https://github.com/Kotlin/kotlinx.coroutines/tree/master/kotlinx-coroutines-test
 * https://stackoverflow.com/questions/62110761/unit-test-the-new-kotlin-coroutine-stateflow
 * https://github.com/square/okhttp/tree/master/mockwebserver
 *
 */
class AuthModelTest {

    companion object {
        @BeforeClass
        @JvmStatic
        fun onlyOnce() {
            Timber.plant(object : Timber.Tree() {
                override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                    println("[T$priority${tag?.let { "|$it" } ?: ""}]:$message")
                }
            })
        }
    }

    private lateinit var mockWebServer: MockWebServer
    private val expectedCredentials = Credentials("anIdToken", "anAnonymousUserId", "aRefreshToken")

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    /**
     * This is an example of testing model behaviour.
     *
     * This tests the 'first use' scenario, where no credentials were loaded,
     * and AuthModel should sign the users up anonymously.
     *
     * This should transition us from INIT -to-> READY,
     * and we validate that the correct info was fetched from our MockWebServer.
     *
     * The timeout value below is for cases where the flows don't return the
     * number of expected items, and other timing issues.
     */
    @Test
    fun init() = runTest(dispatchTimeoutMs = 3000) {
        // Enqueue an anonymous response.
        mockWebServer.enqueueResponse("r.anonymous.json", 200)

        // When authModel is created with null credentials, it should signInAnonymously
        val authModel = buildTestAuthModel(mockWebServer.url("/").toString())

        // We attempt to take 2 items from the store.
        val states = authModel.authStore.take(2).toList()
        states.forEach {
            println("➡️ collected: $it")
        }
        assertIs<Init>(states[0])
        val ready = states[1]
        assertIs<Ready>(ready)
        assertEquals(expectedCredentials, ready.credentials)
    }

    @Test
    fun refreshToken() = runTest(dispatchTimeoutMs = 3000) {
        val expiredCredentials = Credentials("anExpiredToken", "anAnonymousUserId", "aRefreshToken")
        val expectedCredentials = Credentials("anIdToken", "anAnonymousUserId", "aRefreshToken")
        val authModel = buildTestAuthModel(
            mockWebServer.url("/").toString(),
            expiredCredentials
        )

        launch(CoroutineName("Test.refreshToken.readyJob")) {
            val firstState = authModel.authStore.first()
            Timber.d("startingState: $firstState")

            assertIs<Ready>(firstState)
            assertEquals(expiredCredentials, firstState.credentials)

            launch(CoroutineName("Test.refreshToken.refreshToken()")) {
                val states = authModel.authStore.take(2).toList()
                assertEquals(
                    expiredCredentials,
                    assertIs<Refreshing>(states[0]).credentials
                )
                assertEquals(
                    expectedCredentials,
                    assertIs<Ready>(states[1]).credentials
                )
            }

            // Enqueue a refresh response.
            mockWebServer.enqueueResponse("r.refresh.json", 200)
            authModel.processRefreshToken() // Completes the take(2) above.
        }
    }

}

