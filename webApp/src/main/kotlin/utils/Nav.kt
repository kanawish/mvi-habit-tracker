package utils

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

fun KSCStrings.toLabel(nav: Nav):String = when(nav) {
    Nav.LANDING -> header.navLabel.LANDING
    Nav.BLOG -> header.navLabel.BLOG
    Nav.ABOUT -> header.navLabel.ABOUT
}