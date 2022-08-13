import co.touchlab.kermit.Logger
import com.kanastruk.sample.common.SampleData
import com.kanastruk.sample.common.rest.auth.SecureTokenApi
import com.kanastruk.sample.common.rest.habit.HabitApi
import com.kanastruk.sample.common.rest.ktor.buildAuthClient
import com.kanastruk.sample.common.rest.ktor.buildHabitClient
import io.ktor.client.engine.cio.*
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.Serializable
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class KtorJvmTest:SecureTokenApi, HabitApi {
    private val log: Logger = Logger.withTag("☕️ KtorJvmTest")

    @BeforeTest
    fun setup() {
        log.d("setup")
    }

    @Test
    fun foo() = runTest {
        KtorLitterbox(CIO).ktorSignUp()
    }

    @Serializable
    data class Data(
        val a: List<Int>,
        val b: Set<Int>
    )

    @Test
    fun bar() = runTest {

        // Hmm
        val data = Json.decodeFromString<Data>("""
        {
            "a": [42, 42],
            "b": [42, 42]
        }
    """)

        val x: Map<String, String> = Json.decodeFromString(
            string = """
                { "a":"1", "b":"2" }
            """.trimIndent()
        )
        
        val authClient = buildAuthClient(CIO)
        val refreshToken = authClient.refreshToken(SampleData.credentials.refreshToken)
        refreshToken.successBody?.toCredentials()?.let { freshCredentials ->
            val userId = freshCredentials.localId
            buildHabitClient(CIO, freshCredentials.idToken).apply {
                val habits = getHabits(userId) // empty at first.
                val entries = getEntries(userId)
                log.d("habits:$habits\nentries:$entries")
                // ... moar testing
            }
        }
    }

    @AfterTest
    fun tearDown() {
        log.d("tearDown")
    }

}