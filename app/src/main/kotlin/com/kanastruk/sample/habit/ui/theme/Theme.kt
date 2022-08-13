package com.kanastruk.sample.habit.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.kanastruk.sample.habit.R

@Composable
private fun darkColorPalette() = darkColors(
    primary = colorResource(id = R.color.primaryDarkColor),
    primaryVariant = colorResource(id = R.color.primaryColor),
    secondary = colorResource(id = R.color.secondaryDarkColor),
    secondaryVariant = colorResource(id = R.color.secondaryDarkColor),
    background = Color(0xFF121212),
    surface = Color(0xFF121212),
    error = Color(0xFFCF6679),
    onPrimary = colorResource(id = R.color.primaryTextColor),
    onSecondary = colorResource(id = R.color.secondaryTextColor),
    onBackground = Color.White,
    onSurface = Color.White,
    onError = Color.Black
)

@Composable
private fun lightColorPalette() = lightColors(
    primary = colorResource(id = R.color.primaryLightColor),
    primaryVariant = colorResource(id = R.color.primaryColor),
    secondary = colorResource(id = R.color.secondaryLightColor),
    secondaryVariant = colorResource(id = R.color.secondaryColor),
    background = Color.White,
    surface = Color.White,
    error = Color(0xFFB00020),
    onPrimary = colorResource(id = R.color.primaryTextColor),
    onSecondary = colorResource(id = R.color.secondaryTextColor),
    onBackground = Color.Black,
    onSurface = Color.Black,
    onError = Color.White

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        fun darkDefault() {
            val primary = Color(0xFFBB86FC)
            val primaryVariant = Color(0xFF3700B3)
            val secondary = Color(0xFF03DAC6)
            val secondaryVariant = secondary
            val background = Color(0xFF121212)
            val surface = Color(0xFF121212)
            val error = Color(0xFFCF6679)
            val onPrimary = Color.Black
            val onSecondary = Color.Black
            val onBackground = Color.White
            val onSurface = Color.White
            val onError = Color.Black
        }

        darkColors(
            primary = Color(0xFFe3c54a),
            onPrimary = Color.Black,
            primaryVariant = Color(0xFF554500),
            secondary = Color(0xFFd2c6a2),
            secondaryVariant = Color(0xFF4e462a),
            surface = Color(0xFF121212)
        )

    } else {
        fun lightDefault() {
            val primary = Color(0xFF6200EE)
            val primaryVariant = Color(0xFF3700B3)
            val secondary = Color(0xFF03DAC6)
            val secondaryVariant = Color(0xFF018786)
            val background = Color.White
            val surface = Color.White
            val error = Color(0xFFB00020)
            val onPrimary = Color.White
            val onSecondary = Color.Black
            val onBackground = Color.Black
            val onSurface = Color.Black
            val onError = Color.White
        }

        lightColors(
            primary = Color(0xFF976C00),
            primaryVariant = Color(0xFFFFB600),
            onPrimary = Color.White,
            secondary = Color(0xFFFFF4D1),
            secondaryVariant = Color(0xFFFFCE00),
            onSecondary = Color.Black
        )

    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}