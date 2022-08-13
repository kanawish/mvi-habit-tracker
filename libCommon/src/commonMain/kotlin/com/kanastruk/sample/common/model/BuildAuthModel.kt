package com.kanastruk.sample.model

import com.kanastruk.sample.common.model.AuthModel
import com.kanastruk.sample.common.rest.auth.Credentials
import com.kanastruk.sample.common.rest.ktor.buildAuthClient
import io.ktor.client.engine.*
import kotlinx.coroutines.CoroutineScope

/**
 *  AuthModel builder function, might end up moving this to DI when we implement it.
 */
fun <T : HttpClientEngineConfig> buildAuthModel(
    credentials: Credentials?,
    engineFactory:HttpClientEngineFactory<T>,
    scope: CoroutineScope
): AuthModel {
    return AuthModel(credentials, buildAuthClient(engineFactory), scope)
}
