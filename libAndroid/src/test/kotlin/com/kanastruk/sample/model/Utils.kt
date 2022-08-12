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


/**
 * Utility method that queues up a response built from a JSON file in our testing resources folder.
 */

/**
 *  AuthModel builder function, might end up moving this to DI when we implement it.
 */

/** Test authModel builder */

/**
 * HabitApi builder function, might end up moving this to DI when we implement it.
 */

/** Test habitModel builder */
