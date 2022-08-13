import co.touchlab.kermit.Logger
import com.kanastruk.sample.common.rest.auth.AnonymousResponse
import com.kanastruk.sample.common.rest.auth.Credentials
import com.kanastruk.sample.common.rest.auth.IdentityToolkitApi
import com.kanastruk.sample.common.rest.auth.SecureTokenApi
import com.kanastruk.sample.common.rest.ktor.buildAuthClient
import io.ktor.client.engine.*
import okio.FileSystem
import okio.Path
import okio.buffer
import okio.use

/**
 * https://ktor.io/docs/http-client-engines.html#minimal-version
 * @param engineFactory
 */
class KtorLitterbox<T : HttpClientEngineConfig>(engineFactory: HttpClientEngineFactory<T>) :
    IdentityToolkitApi, SecureTokenApi {
    private val log: Logger = Logger.withTag("🐈\uD83D\uDCA8 KtorLitterbox")

    private val authClient = buildAuthClient(engineFactory)

    suspend fun ktorFooFoo() {
        log.d("ktorFooFoo")
    }

    suspend fun ktorSignUp() {
        log.d("ktorSignUp")
        val sr = authClient.signupAnonymous()
        log.d("httpResponse: ${sr.response}")
        val ctr: Any? = if (sr.isSuccessful) sr.successBody else sr.errorBody
        log.d("cast signup response: $ctr")
        when (ctr) {
            is AnonymousResponse -> {
                val rr = authClient.refreshToken(ctr.refreshToken)
                val crr: Any? = if (rr.isSuccessful) rr.successBody else rr.errorBody
                log.d("cast refresh response: $crr")
            }
            else -> {
                log.d("signUp failed, can't refresh.")
            }
        }
    }

    /**
     * Returns 'refreshed' credentials.
     */
    suspend fun ktorRefresh(credentials: Credentials): Credentials? {
        log.d("ktorRefresh credentials")
        val refResp = authClient.refreshToken(credentials.refreshToken)
        return if (refResp.isSuccessful) refResp.successBody?.toCredentials() else null
    }

    /** Unused, but would be nice to have a multiplat JSON encode/decode */
    fun FileSystem.readFile(path: Path): String {
        return source(path).use { fileSource ->
            fileSource.buffer().readUtf8()
        }
    }

}

