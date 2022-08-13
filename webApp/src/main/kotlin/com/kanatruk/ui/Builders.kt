package com.kanastruk.ui

import kotlinx.browser.document
import kotlinx.dom.addClass
import kotlinx.dom.removeClass
import kotlinx.html.*
import kotlinx.html.dom.append
import kotlinx.html.dom.create
import org.w3c.dom.Document
import org.w3c.dom.HTMLElement

/**
 * com.kanastruk.ui.appendContent manipulates the DOM to match our typical
 * 'header-main-footer' layout.
 *
 * @param block will be appended to the 'main' element
 *
 * @return the main HTMLElement, to allow for further manipulation by caller.
 */
fun Document.appendContent(
    header: HTMLElement,
    footer: HTMLElement,
    main: HTMLElement = document.create.main("container"),
    block: TagConsumer<HTMLElement>.() -> Unit = {}
): HTMLElement {
    main.append {
        this.block()
    }

    body?.apply {
        append {
            div("container-fluid px-0").apply {
                append(header)
                append(main)
                footer.addClass(
                    "container",
                    "text-black-50"
                )
                append(footer)
            }
        }
    }

    return main
}

fun Document.buildBackdropCarousel(carouselId:String, images:List<String>): HTMLElement {
    return create
        .div("carousel slide carousel-fade") {
            style = "background:#00000050;"
            id = carouselId
            attributes["data-bs-ride"] = "carousel"
            div("carousel-inner") {
                images.forEach { imageSrc ->
                    div("carousel-item vh-50") {
                        style = "z-index: -1;"
                        if (images.first() == imageSrc) classes += "active"
                        attributes["data-bs-interval"] = "10000"
                        img(src = imageSrc, classes = "vh-50") {
                            style = "width:100%;object-fit:cover;"
                        }

                    }
                }
            }
        }
}

fun Document.buildCarousel(carouselId:String, slides: List<SlideData> = emptyList()):HTMLElement {
    return create
        // TODO: Add controls.
        .div("carousel slide carousel-fade") {
            id = carouselId
            attributes["data-bs-ride"] = "carousel"
            div("carousel-inner") {
                slides.forEach { slide ->
                    carouselSlide(slide, slide == slides.first())
                }
            }
        }
}

private fun DIV.carouselSlide(slide: SlideData, active:Boolean=false) {
    div("carousel-item") {
        if(active) classes += "active"
        attributes["data-bs-interval"] = "10000"
        img(src=slide.imgSrc) {
            slide.imgStyle?.let { style = it }
        }
        div(classes = "carousel-caption") {
            if( slide.imgLogoSrc!=null ) {
                classes += "text-center"
                img(src = slide.imgLogoSrc, classes = "mb-3") { width = "15%" }
                h2 { +slide.title }
                p { +slide.description }

            } else {
                classes += "text-center"
                h2 { +slide.title }
                p { +slide.description }
            }
        }
    }
}

/*
fun Document.createWrapInDiv(wrapDivClass:String, wrapped: HTMLElement): HTMLElement {
    return create
        .div(wrapDivClass)
        .apply { append(wrapped) }
}
*/

/**
 * com.kanastruk.ui.buildCoverPage manipulates the DOM to create a centered "Cover Page"
 * with a fixed width and height that will match the available real-estate.
 */
fun Document.buildCoverPage(header: HTMLElement, data: CoverPageData, footer: HTMLElement) {
    // Targets HTML to 'cap' height to 100%
    documentElement?.addClass("h-100")
    // Header / Main / Footer
    body?.apply {
        addClass("d-flex", "h-100", "text-center", "text-white", "bg-dark")
        append {
            div("d-flex w-100 h-100 pt-0 p-3 mx-auto flex-column") {
                main("cover-container container pt-3 text-center") {
                    h1 { +data.title }
                    p("lead") { +data.description }
                }
            }.apply {
                header.addClass("mb-auto")
                prepend(header)
                footer.addClass("text-white-50")
                append(footer)
            }
        }
    } ?: console.error("No body?")
}

val mockSlides = listOf(
    SlideData(
        "Foo",
        "Foo description",
        "images/asher-ward-18oNt_MP8M0-unsplash.jpg",
        "width: inherit; object-fit: cover; object-position: 100% 70%;",
        imgLogoSrc = "images/kanastruk-logo.svg"

    ), SlideData(
        "Bar",
        "Bar description",
        "images/nathalia-segato-ZIDYcXO-PXM-unsplash.jpg",
        "width: inherit; object-fit: cover; object-position: 100% 70%;",
        "images/kanastruk-logo.svg"
    )
)

fun Document.buildScalingLandingPage(
    carouselId: String,
    header: HTMLElement,
    elements:List<HTMLElement>,
    footer: HTMLElement
) {
    // TODO: replace header with builder?
    header.removeClass("bg-dark")
    header.style.background = "#00000080"

    val strings = listOf(
        "images/steven-wright-cbOgtT3iLtM-unsplash.jpg.webp",
        "images/nikita-vantorin-F6Gk6nhwlmY-unsplash.jpg.webp",
        "images/matthias-mullie-VAxCHgJvZ0g-unsplash.jpg.webp",
        "images/mike-kienle-CatVdRYLBBU-unsplash.jpg.webp"
    )
    val backdropCarousel = buildBackdropCarousel(carouselId, strings)
    val overlay = create.div("w-100") {
        style = "position: absolute;"
        img(classes = "w-100 vh-50") {
            src = "images/kanastruk-logo.svg"
        }
//        h2 { +data.title }
//        p { +data.description }
    }
    // overlay.prepend(header)

    body?.apply {
        append {
            div("shadow-lg d-flex flex-column vh-50 text-center text-white").apply {
                append(backdropCarousel)
                append(overlay)
            }
        }
        // TODO: Below-fold divs...
        elements.forEach { append(it) }

        append(footer)
    } ?: console.error("No body?")
}

fun Document.buildScalingLandingPageV1(header: HTMLElement, data: CoverPageData, footer: HTMLElement) {
    // Targets HTML to 'cap' height to 100%
    documentElement?.addClass()

    val fooCarousel = buildCarousel(
        "mainCarousel", mockSlides
    )

    // Header / Main / Footer
    body?.apply {
        append {
            val main = main("text-center") {
                h1 { +data.title }
                p("lead") { +data.description }
            }
            main.append(fooCarousel)
            div("d-flex flex-column vh-95 text-center text-white bg-dark").apply {
                append(header)
                append(main)
            }

            val x: HTMLElement = div() {
                h1 { +"Other content"}
            }
            append(x)
//            x.append(fooCarousel)
            footer.addClass("text-white-50")
            append(footer)
        }
    } ?: console.error("No body?")

}

/**
 * com.kanastruk.ui.createFooter creates and returns a footer for use by our other 'builder' functions.
 */
fun Document.createFooter(classes: String, copyrightNotice: String): HTMLElement {
    return create.footer(classes) {
        p { +copyrightNotice }
        // TODO: Confidentiality / Terms
    }
}
