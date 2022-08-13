package com.kanastruk.sample.habit.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.kanastruk.sample.habit.R

val SpartanFamily = FontFamily(
    Font(R.font.leaguespartan_black, FontWeight.Black),
    Font(R.font.leaguespartan_bold, FontWeight.Bold),
    Font(R.font.leaguespartan_extrabold, FontWeight.ExtraBold),
    Font(R.font.leaguespartan_extralight, FontWeight.ExtraLight),
    Font(R.font.leaguespartan_light, FontWeight.Light),
    Font(R.font.leaguespartan_medium, FontWeight.Medium),
    Font(R.font.leaguespartan_regular, FontWeight.Normal),
    Font(R.font.leaguespartan_semibold, FontWeight.SemiBold),
    Font(R.font.leaguespartan_thin, FontWeight.Thin),
)

val NunitoFamily = FontFamily(
    Font(R.font.nunitosans_black, FontWeight.Black),
    Font(R.font.nunitosans_bold, FontWeight.Bold),
    Font(R.font.nunitosans_extralightitalic, FontWeight.ExtraLight, FontStyle.Italic),
    Font(R.font.nunitosans_regular, FontWeight.Normal)
)

// NOTE: https://learnui.design/blog/ultimate-guide-similar-fonts.html
// Set of Material typography styles to start with
val Typography = Typography(
    h1 = TextStyle(fontFamily = NunitoFamily, fontWeight = FontWeight.Black, fontSize = 32.sp),
    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)