package com.kanastruk.sample.model

import com.kanastruk.fb.rest.HabitApi
import com.kanastruk.sample.firebase.FirebaseConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * HabitApi builder function, might end up moving this to DI when we implement it.
 */
fun buildHabitApi(
    authToken: String,
    url: String = FirebaseConfig.RTDB_URL // TODO: Fix this up.
): HabitApi {
    // TODO: Should handle exceptions and reflect in the model.
    fun buildClient(authToken: String): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val url = chain
                .request().url
                .newBuilder()
                .addQueryParameter("auth", authToken)
                .build()
            chain.proceed(chain.request().newBuilder().url(url).build())
        }
        .addInterceptor(HttpLoggingInterceptor().apply { setLevel(HttpLoggingInterceptor.Level.BASIC) })
        .build()

    return Retrofit.Builder()
        .baseUrl(url)
        .client(buildClient(authToken))
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(HabitApi::class.java)
}
