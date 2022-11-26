@file:OptIn(ExperimentalCoroutinesApi::class)

package com.kanastruk.sample.model

import com.kanastruk.fb.rest.auth.Credentials
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
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

/** Test authModel builder */
fun CoroutineScope.buildTestAuthModel(mockUrlString: String, credentials: Credentials? = null): AuthModel {
    return buildAuthModel(credentials, mockUrlString, mockUrlString, this)
}

/** Test habitModel builder */
fun CoroutineScope.buildTestHabitModel(urlString:String, authModel: AuthModel): HabitModel {
    return HabitModel(authModel, { authToken -> buildHabitApi(authToken, urlString) }, this)
}
