package utils

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString

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
}

fun String.toKSCStrings(): KSCStrings = kotlinx.serialization.json.Json.decodeFromString(this)
