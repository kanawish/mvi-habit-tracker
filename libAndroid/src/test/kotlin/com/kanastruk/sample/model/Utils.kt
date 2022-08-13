@file:OptIn(ExperimentalCoroutinesApi::class)

package com.kanastruk.sample.model

import com.kanastruk.sample.common.model.AuthModel
import com.kanastruk.sample.common.model.HabitModel
import com.kanastruk.sample.common.rest.auth.Credentials
import com.kanastruk.sample.common.rest.ktor.buildHabitClient
import io.ktor.client.engine.cio.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import java.nio.charset.StandardCharsets


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
    return buildAuthModel(credentials, CIO, this)
}

/** Test habitModel builder */
fun CoroutineScope.buildTestHabitModel(urlString:String, authModel: AuthModel): HabitModel {
    return HabitModel(authModel, { authToken -> buildHabitClient(CIO, authToken) }, this)
}
