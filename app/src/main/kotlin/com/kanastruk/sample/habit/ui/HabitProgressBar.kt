package com.kanastruk.sample.habit.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kanastruk.sample.habit.ui.theme.SpartanFamily

/**
 * After seeing it in action, this is not terrible, but it's not great either.
 *
 * I think we'd be better served by using standard Row item with Android UX.
 *
 * 'À la gmail' where swipes still work to add/undo entries, and tap opens up
 * a habit detail/editor screen.
 */
@Composable
fun HabitProgressBar(
    boxModifier: Modifier = Modifier,
    habitName: String,
    progress: Float,
    progressDescription: String,
    color: Color
) {
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = boxModifier
    ) {
        val onBackground = MaterialTheme.colors.onBackground
        Canvas(
            modifier = Modifier.fillMaxSize().padding(top = 1.dp)
        ) {
            val w = size.width
            val h = size.height
            val corner = 4.dp.toPx()
            val pad = 0

            val rectOffset = Offset(0f + pad, 0f + pad)
            val rectWidth = w - (2 * pad)
            val rectHeight = h - (2 * pad)
            val rectCornerRadius = CornerRadius(corner, corner)
            drawRoundRect(
                color.copy(alpha = 0.2f).compositeOver(Color.White),
                rectOffset,
                Size(rectWidth, rectHeight),
                rectCornerRadius,
                Fill
            )
            drawRoundRect(
                color.copy(alpha = 0.8f).compositeOver(Color.White),
                rectOffset,
                Size(rectWidth * progress, rectHeight),
                rectCornerRadius,
                Fill
            )
            drawRoundRect(
                onBackground,
                rectOffset,
                Size(rectWidth, rectHeight),
                rectCornerRadius,
                Stroke(1.dp.toPx())
            )
        }
        Text(
            fontFamily = SpartanFamily,
            color = Color.Black,
            text = habitName,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
        Text(
            fontFamily = SpartanFamily,
            color = Color.Black,
            text = progressDescription,
            textAlign = TextAlign.End,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
    }
}