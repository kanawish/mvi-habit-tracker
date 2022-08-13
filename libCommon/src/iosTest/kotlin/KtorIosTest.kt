import co.touchlab.kermit.Logger
import io.ktor.client.engine.cio.*
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class KtorIosTest {
    private val log: Logger = Logger.withTag("🍎 KtorIosTest")

    @BeforeTest
    fun setup() {
        log.d("setup")
    }

    @Test
    fun foo() = runTest {
        log.d("fooA")
        val litterbox = KtorLitterbox(CIO)
        log.d("fooB")
        litterbox.ktorFooFoo()
    }

    @AfterTest
    fun tearDown() {
        log.d("tearDown")
    }
}