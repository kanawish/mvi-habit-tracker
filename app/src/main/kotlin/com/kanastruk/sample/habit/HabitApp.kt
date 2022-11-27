package com.kanastruk.sample.habit

import android.app.Application
import android.content.Context
import com.kanastruk.fb.rest.HabitApi
import com.kanastruk.fb.rest.auth.Credentials
import com.kanastruk.sample.habit.ui.getCredentials
import com.kanastruk.sample.habit.ui.setCredentials
import com.kanastruk.sample.model.AuthModel
import com.kanastruk.sample.model.AuthState
import com.kanastruk.sample.model.HabitModel
import com.kanastruk.sample.model.buildAuthModel
import com.kanastruk.sample.model.buildHabitApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
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
            buildAuthModel(credentials = loadCredentials(), scope = get())
        }
        single<(String) -> HabitApi> { { token -> buildHabitApi(token) } }
        single {
            val authModel = get<AuthModel>()
            val habitApiBuilder = get<(authToken: String) -> HabitApi>()
            val scope = get<CoroutineScope>()
            HabitModel(authModel, habitApiBuilder, scope)
        }
    }

    private val authModel:AuthModel by inject()

    override fun onCreate() {
        super.onCreate()

        Timber.plant(DebugTree())

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
