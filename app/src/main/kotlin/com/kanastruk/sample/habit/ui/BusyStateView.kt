package com.kanastruk.sample.habit.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kanastruk.sample.habit.ui.theme.SpartanFamily

@Composable
fun BusyStateView(label: String) {
    LogCompositions(tag = "Busy", msg = "Recomposed $label")
    Scaffold {
        Column(modifier = Modifier.padding(vertical = 16.dp)) {
            Row(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = label,
                    fontFamily = SpartanFamily,
                    fontWeight = FontWeight.Black,
                    fontSize = 32.sp
                )
                Spacer(Modifier.weight(1f))
            }
        }
    }
}
