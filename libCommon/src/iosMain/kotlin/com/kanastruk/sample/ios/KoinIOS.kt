package com.kanastruk.sample.ios

import com.kanastruk.sample.common.SampleData
import com.kanastruk.sample.common.model.AuthModel
import com.kanastruk.sample.common.model.HabitModel
import com.kanastruk.sample.common.rest.auth.Credentials
import com.kanastruk.sample.common.rest.ktor.buildHabitClient
import com.kanastruk.sample.model.buildAuthModel
import com.russhwolf.settings.AppleSettings
import com.russhwolf.settings.Settings
import io.ktor.client.*
import io.ktor.client.engine.darwin.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.module
import platform.Foundation.NSUserDefaults

fun initKoinIos(
    userDefaults: NSUserDefaults,
    appInfo: AppInfo
): KoinApplication = startKoin {
    modules(
        module {
            single<Settings> { AppleSettings(userDefaults) }
            single { appInfo }
        },
        appModule
    )
}

interface AppInfo {
    val appId: String
}

private val appModule = module {
    single { MainScope() } // Simple/naive CoroutineScope for model.
    single<() -> Credentials?> { { SampleData.credentials } } // TODO: Use iOS `Settings` instead.
    single {
        val loadCredentials: () -> Credentials? = get()
        buildAuthModel(loadCredentials(), Darwin, get())
    }
    single<(String) -> HttpClient> { { token -> buildHabitClient(Darwin, token) } }
    single {
        val authModel = get<AuthModel>()
        val habitApiBuilder = get<(authToken: String) -> HttpClient>()
        val scope = get<CoroutineScope>()
        HabitModel(authModel, habitApiBuilder, scope)
    }
}

