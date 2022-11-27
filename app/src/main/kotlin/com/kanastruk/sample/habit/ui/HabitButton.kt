package com.kanastruk.sample.habit.ui

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun RowScope.HabitButton(imageVector: ImageVector, handler:()->Unit) {
    IconButton(
        modifier = Modifier
            .size(48.dp)
            .align(Alignment.CenterVertically),
        onClick = handler
    ) { Icon(imageVector, contentDescription = null) }
}