package com.kanastruk.sample.common.rest.auth

import com.kanastruk.sample.common.rest.TypedResponse
import com.kanastruk.sample.common.rest.buildTypedResponse
import com.kanastruk.sample.common.rest.ErrorResponse
import com.kanastruk.sample.firebase.FirebaseConfig
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

/**
 * SLIDE: Thinking on these 'functional interfaces', I think there's a compelling
 *  parallel here with "ViewModel" or "Dispatchers" and so on.
 * SLIDE: Core idea, leveraging functional programming as a 'connector' layer between
 *  M<->V, instead of layers stateful objects in your architecture.
 * SLIDE: Something to be said re: interface vs class(val httpClient), it depends on
 *  choices you make re: DI, and ultimately is a question of tastes/dev team culture.
 *
 * The IdentityToolkitApi Ktor interfaces.
 * see https://firebase.google.com/docs/reference/rest/auth#section-sign-in-anonymously
 */
interface IdentityToolkitApi {
    suspend fun HttpClient.signupAnonymous(): TypedResponse<AnonymousResponse?, ErrorResponse> {
        return FirebaseConfig.run {
            val signUpPath = "v1/accounts:signUp"
            val hr: HttpResponse = post(IDENTITY_TOOLKIT_ENDPOINT + signUpPath) {
                url { parameters.append("key", API_KEY) }
                contentType(ContentType.Application.Json)
                setBody(IdentityToolkitParam())
            }
            hr.buildTypedResponse()
        }
    }
}
