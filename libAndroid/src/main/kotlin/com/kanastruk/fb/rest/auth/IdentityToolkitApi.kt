package com.kanastruk.fb.rest

import com.kanastruk.sample.common.rest.auth.AnonymousResponse
import com.kanastruk.sample.common.rest.auth.IdentityToolkitParam
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * The IdentityToolkitApi Retrofit interface definition.
 *
 * see https://firebase.google.com/docs/reference/rest/auth#section-sign-in-anonymously
 */
interface IdentityToolkitApi {
    @Headers("Content-Type: application/json")
    @POST("v1/accounts:signUp")
    suspend fun signupAnonymous(
        @Query("key") apiKey: String,
        @Body param: IdentityToolkitParam
    ): Response<AnonymousResponse>
}
