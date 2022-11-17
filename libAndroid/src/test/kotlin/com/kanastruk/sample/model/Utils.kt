@file:OptIn(ExperimentalCoroutinesApi::class)

package com.kanastruk.sample.model

import com.kanastruk.fb.rest.HabitApi
import com.kanastruk.fb.rest.IdentityToolkitApi
import com.kanastruk.fb.rest.auth.Credentials
import com.kanastruk.fb.rest.auth.SecureTokenApi
import com.kanastruk.sample.android.LibAndroid
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.nio.charset.StandardCharsets

val tree = object : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        println("[T$priority${tag?.let { "|$it" } ?: ""}]:$message")
    }
}

/**
 * Utility method that queues up a response built from a JSON file in our testing resources folder.
 */
internal fun MockWebServer.enqueueResponse(fileName: String, code: Int) {
    val body: String? = javaClass.classLoader
        ?.getResourceAsStream("api-response/$fileName")
        ?.run { source().buffer().readString(StandardCharsets.UTF_8) }

    enqueue(
        MockResponse()
            .setResponseCode(code)
            .apply { body?.let { setBody(body) } } // Optional body.
    )
}

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
        // .addInterceptor(loggingInterceptor)
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

/** Test authModel builder */
fun CoroutineScope.buildTestAuthModel(mockUrlString: String, credentials: Credentials? = null): AuthModel {
    return buildAuthModel(credentials, mockUrlString, mockUrlString, this)
}

/**
 * HabitApi builder function, might end up moving this to DI when we implement it.
 */
fun buildHabitApi(
    authToken: String,
    url: String = LibAndroid.RTDB_URL
) : HabitApi {
    fun buildClient(authToken:String): OkHttpClient = OkHttpClient
        .Builder()
        .addInterceptor { chain ->
            val url = chain
                .request().url
                .newBuilder()
                .addQueryParameter("auth", authToken)
                .build()
            chain.proceed(chain.request().newBuilder().url(url).build())
        }
        // .addInterceptor(HttpLoggingInterceptor().apply { setLevel(HttpLoggingInterceptor.Level.BASIC) })
        .build()

    return Retrofit
        .Builder()
        .baseUrl(url)
        .client(buildClient(authToken))
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(HabitApi::class.java)
}

/** Test habitModel builder */
fun CoroutineScope.buildTestHabitModel(urlString:String, authModel: AuthModel): HabitModel {
    return HabitModel(authModel, { authToken -> buildHabitApi(authToken, urlString) }, this)
}
