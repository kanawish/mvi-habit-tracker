package com.kanastruk.sample.common.model

import co.touchlab.kermit.Logger
import com.kanastruk.mvi.intent.Intent
import com.kanastruk.mvi.intent.expectingIntent
import com.kanastruk.sample.common.rest.auth.*
import io.ktor.client.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * AuthModel
 * - Should automatically signIn when missing existing credentials.
 * - Should expose a 'refreshToken()' Intent
 */
class AuthModel(
    existingCredentials: Credentials? = null,
    private val httpClient: HttpClient,
    scope: CoroutineScope
): IdentityToolkitApi, SecureTokenApi, CoroutineScope by scope {
    private val _authStore = MutableStateFlow<AuthState>(AuthState.Init)
    val authStore = _authStore.asStateFlow()
    val log = Logger.withTag("🔐")

    init {
        log.d("AuthModel.init()")

        launch {
            _authStore.value = when(val credentials = existingCredentials ?: signInAnonymous()) {
                null -> AuthState.Error("Anonymous sign-in failed.")
                else -> AuthState.Ready(credentials)
            }
        }
    }

    private suspend fun signInAnonymous(): Credentials? {
        val response = httpClient.signupAnonymous()
        return if (response.isSuccessful) response.successBody?.toCredentials() else null
    }

    private fun process(intent: Intent<AuthState>) {
        _authStore.value = intent.reduce(_authStore.value)
    }

    fun processRefreshToken() = process(
        expectingIntent { old: AuthState.Ready ->
            // Launch the async refresh call.
            launch {
                val response = httpClient.refreshToken(refreshToken = old.credentials.refreshToken)
                // When complete, assigns resulting AuthState to the store.
                _authStore.value = response.successBody?.toCredentials()?.let { refreshedCredentials ->
                    AuthState.Ready(refreshedCredentials)
                } ?: AuthState.Error("Failed to refresh credentials")
            }
            // The AuthModel will be busy `Refreshing` until coroutine call completes.
            AuthState.Refreshing(old.credentials)
        }
    )

}