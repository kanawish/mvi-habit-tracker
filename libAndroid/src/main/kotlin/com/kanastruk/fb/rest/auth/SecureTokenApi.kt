package com.kanastruk.fb.rest.auth

import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Allows us to refresh expired session Credentials.
 */
interface SecureTokenApi {
    @FormUrlEncoded
    @POST("v1/token")
    suspend fun refreshToken(
        @Query("key") apiKey: String,
        @Field("grant_type") grantType: String,
        @Field("refresh_token") refreshToken: String
    ): Response<RefreshResponse>
}