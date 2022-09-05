package com.kanastruk.sample.habit

import LibCommonAndroid
import android.app.Application
import android.content.Context
import com.kanastruk.sample.common.rest.auth.Credentials
import com.kanastruk.sample.common.LibCommon
import com.kanastruk.sample.common.model.AuthModel
import com.kanastruk.sample.common.model.AuthState
import com.kanastruk.sample.common.model.HabitModel
import com.kanastruk.sample.common.rest.ktor.buildHabitClient
import com.kanastruk.sample.habit.ui.getCredentials
import com.kanastruk.sample.habit.ui.setCredentials
import com.kanastruk.sample.model.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import org.koin.core.context.startKoin
import org.koin.dsl.module
import timber.log.Timber
import timber.log.Timber.DebugTree

/**
 * Some references on App-related subjects
 *
 * // https://stackoverflow.com/questions/65008486/globalscope-vs-coroutinescope-vs-lifecyclescope
 * // https://proandroiddev.com/android-processlifecycleowner-by-example-2f965061b9da
 * // https://developer.android.com/reference/androidx/lifecycle/ProcessLifecycleOwner
 */
class HabitApp : Application() {
    companion object {
        private const val PREF_KEY = "com.kanastruk.sample.habit"
    }

    /** Initial naive approach to app coroutine scope. */
    private val appScope by lazy { MainScope() }

    private val sharedPreferences by lazy { getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE) }

    private val appModule = module {
        single { CoroutineScope(SupervisorJob() + Dispatchers.IO) } // Simple/naive CoroutineScope for model.
        single<() -> Credentials?> { {
                sharedPreferences.getCredentials()
                    // NOTE: In a real app, never dump credentials to log, of course.
                    .also { c -> Timber.d("Loaded credentials: $c") }
        } }
        single {
            val loadCredentials: () -> Credentials? = get()
            buildAuthModel(loadCredentials(), CIO, get())
        }
        single<(String) -> HttpClient> { { token -> buildHabitClient(CIO, token) } }
        single {
            val authModel = get<AuthModel>()
            val habitApiBuilder = get<(authToken: String) -> HttpClient>()
            val scope = get<CoroutineScope>()
            HabitModel(authModel, habitApiBuilder, scope)
        }
    }

    private val authModel: AuthModel by inject()

    override fun onCreate() {
        super.onCreate()

        Timber.plant(DebugTree())
        Timber.i("HabitApp: ${LibCommonAndroid.ANDROID_COMMON_HELLO} and ${LibCommon.COMMON_HELLO}") // FIXME: Remove

        // TODO: Init
        startKoin {
            modules( appModule )
        }

        // Quick-and-easy credential storage.
        appScope.launch {
            authModel.authStore.collect { authState ->
                when (authState) {
                    // Whenever the authState reports a ready state, we store the associated credentials.
                    is AuthState.Ready -> {
                        // NOTE: In a real app, never dump credentials to log, of course.
                        Timber.d("Attempting to save credentials: ${authState.credentials}")
                        sharedPreferences.setCredentials(authState.credentials)
                    }
                    else -> {} // Ignore
                }
            }
        }

    }
}
