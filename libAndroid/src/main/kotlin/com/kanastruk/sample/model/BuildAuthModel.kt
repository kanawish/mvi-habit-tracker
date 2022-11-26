package com.kanastruk.sample.model

import com.kanastruk.fb.rest.IdentityToolkitApi
import com.kanastruk.fb.rest.auth.Credentials
import com.kanastruk.fb.rest.auth.SecureTokenApi
import com.kanastruk.sample.android.LibAndroid
import kotlinx.coroutines.CoroutineScope
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 *  AuthModel builder function, might end up moving this to DI when we implement it.
 */
fun buildAuthModel(
    credentials: Credentials?,
    idUrl: String = LibAndroid.IDENTITY_TOOLKIT_ENDPOINT,
    secTokenUrl: String = LibAndroid.SECURE_TOKEN_ENDPOINT,
    scope: CoroutineScope
): AuthModel {
    val loggingInterceptor = HttpLoggingInterceptor().apply { setLevel(HttpLoggingInterceptor.Level.BODY) }
    val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    // For sign in
    val identityApi: IdentityToolkitApi = Retrofit.Builder()
        .baseUrl(idUrl)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(IdentityToolkitApi::class.java)

    // For refresh
    val secureTokenApi: SecureTokenApi = Retrofit.Builder()
        .baseUrl(secTokenUrl)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(SecureTokenApi::class.java)

    return AuthModel(credentials, identityApi, secureTokenApi, scope)
}
