package com.kanastruk.sample.model

import com.kanastruk.fb.rest.auth.Credentials
import com.kanastruk.fb.rest.*
import com.kanastruk.fb.rest.auth.AnonymousResponse
import com.kanastruk.fb.rest.auth.SecureTokenApi
import com.kanastruk.mvi.intent.Intent
import com.kanastruk.mvi.intent.expectingIntent
import com.kanastruk.sample.android.LibAndroid
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * AuthModel
 * - Should automatically signIn when missing existing credentials.
 * - Should expose a 'refreshToken()' Intent
 */
class AuthModel(
    existingCredentials: Credentials? = null,
    private val identityApi: IdentityToolkitApi,
    private val secureTokenApi: SecureTokenApi,
    private val scope: CoroutineScope
) {
    private val _authStore = MutableStateFlow<AuthState>(AuthState.Init)
    val authStore = _authStore.asStateFlow()

    init {
        scope.launch {
            _authStore.value = when(val credentials = existingCredentials ?: signInAnonymous()) {
                null -> AuthState.Error("Anonymous sign-in failed.")
                else -> AuthState.Ready(credentials)
            }
        }
    }

    private suspend fun signInAnonymous(): Credentials? {
        val response: Response<AnonymousResponse> =
            identityApi.signupAnonymous(LibAndroid.API_KEY, IdentityToolkitParam())

        return if (response.isSuccessful) response.body()?.toCredentials() else null
    }

    private fun process(intent: Intent<AuthState>) {
        _authStore.value = intent.reduce(_authStore.value)
    }

    fun refreshToken() = process(
        expectingIntent { old: AuthState.Ready ->
            // Launch the async refresh call.
            scope.launch(CoroutineName("AuthModel.refreshToken")) {
                val response = secureTokenApi.refreshToken(
                    apiKey = LibAndroid.API_KEY,
                    grantType = "refresh_token", // TODO: const val
                    refreshToken = old.credentials.refreshToken
                )
                // When complete, assigns resulting AuthState to the store.
                _authStore.value = response.body()?.toCredentials()?.let { refreshedCredentials ->
                    AuthState.Ready(refreshedCredentials)
                } ?: AuthState.Error("Failed to refresh credentials")
            }
            // The AuthModel will be busy `Refreshing` until coroutine call completes.
            AuthState.Refreshing(old.credentials)
        }
    )

}