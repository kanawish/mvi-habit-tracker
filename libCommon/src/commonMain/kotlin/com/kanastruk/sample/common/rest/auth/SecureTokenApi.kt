package com.kanastruk.sample.common.rest.auth

import com.kanastruk.sample.common.rest.TypedResponse
import com.kanastruk.sample.common.rest.buildTypedResponse
import com.kanastruk.sample.common.rest.ErrorResponse
import com.kanastruk.sample.firebase.FirebaseConfig
import io.ktor.client.*
import io.ktor.client.request.forms.*
import io.ktor.http.*

/**
 * Allows us to refresh expired session Credentials.
 */
interface SecureTokenApi {
    suspend fun HttpClient.refreshToken(refreshToken: String): TypedResponse<RefreshResponse?, ErrorResponse> {
        return FirebaseConfig.run {
            // todo: 'submitForm' instead of post?
            val refreshTokenPath = "v1/token"
            val hr = submitForm(
                url = SECURE_TOKEN_ENDPOINT + refreshTokenPath,
                formParameters = Parameters.build {
                    append("grant_type", "refresh_token") // hardcoded, really.
                    append("refresh_token", refreshToken)
                }
            ) {
                url { parameters.append("key", API_KEY) }
                contentType(ContentType.Application.Json)
            }
            hr.buildTypedResponse()
        }
    }
}