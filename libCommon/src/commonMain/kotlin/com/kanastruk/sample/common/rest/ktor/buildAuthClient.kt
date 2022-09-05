package com.kanastruk.sample.common.rest.ktor

import co.touchlab.kermit.Logger
import com.kanastruk.sample.common.rest.auth.Credentials
import com.kanastruk.sample.firebase.FirebaseConfig
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

fun <T : HttpClientEngineConfig> buildAuthClient(engineFactory: HttpClientEngineFactory<T>) =
    HttpClient(engineFactory) {
        val log: Logger = Logger.withTag("🐈💨 buildAuthClient")
        expectSuccess = true
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
        install(Logging) {
            logger = object : io.ktor.client.plugins.logging.Logger {
                override fun log(message: String) {
                    log.v { message }
                }
            }
            level = LogLevel.INFO
        }
        install(HttpTimeout) {
            val timeout = 30000L
            connectTimeoutMillis = timeout
            requestTimeoutMillis = timeout
            socketTimeoutMillis = timeout
        }
    }

// SLIDE: The 2 Ktor client builders are of some interest. (Ktor stuff in general is "MPP" slide worthy)
@OptIn(ExperimentalSerializationApi::class)
fun <T : HttpClientEngineConfig> buildHabitClient(
    engineFactory: HttpClientEngineFactory<T>,
    sessionToken: String,
): HttpClient {
    val log: Logger = Logger.withTag("🐈💨 buildHabitClient")

    return HttpClient(engineFactory) {
        expectSuccess = false // 'true' crashes on 'Auth token is expired'
        // Default request allows us to configure default params for all requests.
        install(DefaultRequest) {
            url(urlString = FirebaseConfig.RTDB_URL)
            url { parameters.append("auth", sessionToken) }
        }
        install(ContentNegotiation) {
            json(Json {
                explicitNulls = false
                prettyPrint = true
                isLenient = true
            })
        }
        install(Logging) {
            logger = object : io.ktor.client.plugins.logging.Logger {
                override fun log(message: String) {
                    log.v { message }
                }
            }
            level = LogLevel.ALL // TODO: Go back to info once proofing is done.
        }
        install(HttpTimeout) {
            val timeout = 30000L
            connectTimeoutMillis = timeout
            requestTimeoutMillis = timeout
            socketTimeoutMillis = timeout
        }
    }
}
