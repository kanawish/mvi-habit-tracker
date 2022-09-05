import com.kanastruk.ui.appendContent
import com.kanastruk.ui.createFooter
import io.kvision.jquery.jQuery
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import kotlinx.html.div
import kotlinx.html.dom.append
import kotlinx.html.dom.create
import kotlinx.html.h1
import kotlinx.html.js.link
import org.w3c.fetch.Response
import utils.debug
import utils.verbose
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.js.Json

/**
 * NOTE: console.log() bug https://youtrack.jetbrains.com/issue/KT-47811
 */
suspend fun fetch(input: String): String {
    return window
        .fetch(input).await()
        .text().await()
}

@JsName("jQuery")
val jQuery = jQuery

/**
 * This was the source of the bloat.
 */
fun bootstrapInit() {
    console.log("bootstrapInit() jQuery == %o", jQuery)
    headAppendCss("css/custom.css")
}

fun headAppendCss(href: String) {
    document.head?.append { link(href, "stylesheet", "text/css") }
}

suspend fun suspendFetch(input: String): Json? {
    verbose { console.log("suspendFetch($input)") }
    return suspendCoroutine { continuation ->
        window.fetch(input)
            .then { res: Response -> res.json().unsafeCast<Json>() }
            .then { json: Json -> continuation.resume(json) }
            .catch { throwable ->
                verbose { console.log(throwable) }
                continuation.resume(null)
            }
    }
}

@DelicateCoroutinesApi
fun main() {
    debug { console.log("ksc main() start") }

    GlobalScope.launch {
        bootstrapInit()
        headAppendCss("css/table-fixed.css")

        val titleHeader = document.create.div("container") {
            h1("pt-3") { +"Habits Tracker" }
        }
        val footer = document.createFooter("mt-auto text-center", "© 2021-2022 Kanastruk service conseil inc.")

        document.appendContent(titleHeader, footer) {
            div("container") { +"I am rows" }
            div("container pb-3") { +"I am controls" }
        }
    }

    console.log("Main completed.")
}
