package com.kanastruk.sample.habit.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LetterButton(letter: Char, handler:()->Unit) {
    OutlinedButton(
        modifier = Modifier.size(32.dp),
        shape = CircleShape,
        contentPadding = PaddingValues(0.dp),
        onClick = handler
    ) {
        Text(
            text = "$letter",
            fontFamily = FontFamily.Monospace,
            fontSize = 18.sp
        )
    }
}