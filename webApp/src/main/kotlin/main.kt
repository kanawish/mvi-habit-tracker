import Nav.*
import com.kanastruk.ui.buildScalingLandingPage
import com.kanastruk.ui.createFooter
import io.kvision.jquery.jQuery
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import kotlinx.html.*
import kotlinx.html.dom.append
import kotlinx.html.dom.create
import kotlinx.html.js.link
import kotlinx.serialization.*
import org.w3c.fetch.Response
import utils.SearchParamHelper
import utils.debug
import utils.verbose
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.js.Json

/**
 * NOTE: console.log() bug https://youtrack.jetbrains.com/issue/KT-47811
 */
suspend fun fetch(input:String): String {
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

fun headAppendCss(href:String) {
    document.head?.append { link(href,"stylesheet","text/css") }
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

@Serializable
data class KSCStrings(
    val header: Header,
    val lang: String,
    val langSelector: LangSelector
) {
    @Serializable
    data class Header(
        val en: String,
        val fr: String,
        val navLabel: NavLabel
    )

    @Serializable
    data class LangSelector(
        val en: Boolean,
        val fr: Boolean
    )

    @Serializable
    data class NavLabel(
        val ABOUT: String,
        val BLOG: String,
        val LANDING: String
    )

    fun toLabel(nav: Nav):String = when(nav) {
        LANDING -> header.navLabel.LANDING
        BLOG -> header.navLabel.BLOG
        ABOUT -> header.navLabel.ABOUT
    }

}

fun String.toKSCStrings(): KSCStrings = kotlinx.serialization.json.Json.decodeFromString(this)

@DelicateCoroutinesApi
fun main() {
    debug { console.log("ksc main() start") }

    GlobalScope.launch {
        bootstrapInit()
        headAppendCss("css/table-fixed.css")

        val titleHeader = document.create.h1 { +"Habits Tracker"}
        val contentRows = document.create.div("container") {
        }
        val controlsDiv = document.create.div("container") {  }

        val mainDiv = document.create.main("container-fluid").apply {  }
        document.append(mainDiv)

        val header = document.create.h1 {}
        val list = document.create.div {  }
        val hero = document.create.div("container col-xxl-8 px-4 mt-5 text-center") {
            h1("display-4 fw-bold") { +"Header test" }
            div("mx-auto") {
                p("lead mb-4") { +"""For modern applications, targeting multiple platforms has become table-stakes. And that's a big challenge when working with limited resources. Kotlin Multiplatform is a promising solution to this problem, allowing you to target iOS, Android and the web while keeping development costs low."""}
            }
            div() {// "overflow-hidden"
                // style = "max-height: 30vh;"
                div("container px-5") {
                    img(classes = "img-fluid border rounded-3 shadow-lg mb-4") {
                        src = "images/kotlin-webpage-screenshot.png"
                        alt = "Kotlin Screenshot"
                        attributes["loading"] = "lazy"
                    }
                }
            }
        }

        val services = document.create.div("shadow bg-dark text-light") {
            div("container col-xxl-8 px-4 py-5") {
                div("row  align-items-center g-5 py-5") {
                    div("col-10 col-sm-6 col-lg-4") {
                        img(classes = "d-block mx-lg-auto img-fluid") {
                            src = "images/kotlin-full-color-mark.png"
                            alt = "Kotlin Logo"
                            width = "700"
                            height = "500"
                            attributes["loading"] = "lazy"
                        }
                    }
                    div("col-lg-8") {
                        h1("display-5 fw-bold lh-1 mb-3") { +"""Our Services""" }
                        p("lead") { +"""Kanastruk offers top-tier Kotlin training and consulting services. Turbocharge your developer team with Kotlin, one of the fastest-growing and most loved programming languages. """ }
                    }
                }
            }
        }
        val about = document.create.div("container col-xxl-8 px-4 py-5") {
            div("row") {
                div("align-items-center g-5") {
                    img(classes = "shadow d-block img-fluid float-end ms-4") {
                        src = "images/22-06_GDE_ProfileOverlay.png"
                        alt = "Bootstrap Themes"
                        width = "150"
                        attributes["loading"] = "lazy"
                    }
                    h1("display-5 fw-bold lh-1 mb-3") { +"""Get in touch!""" }
                    p("lead") { +"""Our Technical Founder is Etienne Caron, with over 25 years of experience in the industry. Previously at Intel and Shopify, Etienne is also a certified Google Developer Expert since 2014.""" }
                    p("fw-bold") { +"""Monday to Friday, from 9:30 AM to 4:30 PM EST""" }
                    a(classes = "btn btn-lg btn-success") {
                        href = "tel:514-320-0163"
                        target = ""
                        +"""Call Us"""
                        i("ms-2 bi-telephone") {}
                    }
                    a(classes = "ms-2 btn btn-lg btn-primary") {
                        href = "mailto:etienne@kanastruk.com?subject=Inquiry"
                        target = ""
                        +"""Email Us"""
                        i("ms-2 bi-mailbox") {}
                    }
                }
            }
        }

        val footer = document.createFooter("mt-3 text-center", "© 2021-2022 Kanastruk service conseil inc.")

/*
        document.buildScalingLandingPage(
            "mainCarousel",
            document.create.div {}, // Cut out header just for test
            listOf(hero,services,about),
            footer
        )
*/

        js("new bootstrap.Carousel('#mainCarousel')")
    }

    console.log("Main completed.")
}

enum class Nav {
    // TODO: i18n for labels, load from i18n map using enum name as key.
    LANDING, BLOG, ABOUT ;

    fun toSearchParam() = "?${NAV_KEV}=${this.name}"
    companion object {
        const val NAV_KEV = "nav"
        fun fromSearchParams(): Nav? {
            val nav = navKeyParam()
            // https://stackoverflow.com/questions/4557387/is-a-url-query-parameter-valid-if-it-has-no-value
            return nav?.let(::valueOf)
        }

        fun navKeyParam(): String? {
            return SearchParamHelper.currentSearchParams().get(NAV_KEV)
        }
    }
}

