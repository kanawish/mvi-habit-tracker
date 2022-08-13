package com.kanastruk.sample.habit.ui

import androidx.annotation.ColorRes
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import com.kanastruk.sample.habit.R

data class ColorOption(val label: String, @ColorRes val color: Int)

@Composable
fun ColorOption.toIconOption(): IconOption {
    val vector = ImageVector.vectorResource(R.drawable.ic_baseline_square_24)

    val icon: @Composable () -> Unit = {
        Icon(
            imageVector = vector,
            contentDescription = null,
            tint = colorResource(id = color)
        )
    }

    return IconOption(label = label, icon = icon)
}

@Composable
fun ColorOption.color(): Color = colorResource(id = color)
data class IconOption(val label: String, val pickHandler: () -> Unit = {}, val icon: (@Composable () -> Unit)? = null)