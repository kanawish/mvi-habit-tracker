package com.kanastruk.sample.habit.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kanastruk.sample.habit.ui.AuthViewEvent.AttemptReloadClick
import com.kanastruk.sample.habit.ui.AuthViewEvent.RefreshCredentialsClick
import com.kanastruk.sample.habit.ui.theme.SpartanFamily

/**
 * TODO: Formalize these talking points in deck.
 *
 * Here's a (contrived) example for "UI state". Some key points:
 * - We have a two step process with our Models to 'get out' of a FAILED state:
 *      1. Refresh the AuthModel session credentials.
 *      2. Reload the HabitModel to get [hopefully] out of 'FAILED' state.
 * - This UI effectively should prevent bad interactions
 *      (double-operator key system example?)
 * - It has internal state towards this, and also reads external state
 *      (more complex in fact than the form-fill)
 * - Cons: Fine line to walk.
 *      (A UI-friendly API could be worked out)
 *      (Stateful IU can get hard to debug)
 * - Pros: Adaptability
 *      (You can't always adjust a Model on the fly)
 *      (Adjusting a model has it's own risks)
 *      (If you end up with 'pure view-concerns logic' in a model)
 *      (it makes your model brittle and non-reusable.)
 *
 * It's important to use critical thinking, iterate and review.
 *
 * NOTE: In the interest of time I've skipped testing the 'failed to reload' flow.
 *  The assumption is that cycling cache from "FAILED" to "LOADING" means we'll be
 *  seeing a new ProposeRefresh composition if LOADING results in FAILED again.
 */
@Composable
fun ProposeRefresh(label: String, refreshing: Boolean, clickHandler: (AuthViewEvent) -> Unit) {
    LogCompositions(tag = "Busy", msg = "Recomposed $label")
    var refreshed by remember { mutableStateOf(false) }
    Scaffold {
        val v16dp = Modifier.padding(vertical = 16.dp)
        Column(modifier = v16dp) {
            val h16dp = Modifier.padding(horizontal = 16.dp)
            Row(modifier = h16dp) {
                Text(
                    text = label,
                    fontFamily = SpartanFamily,
                    fontWeight = FontWeight.Black,
                    fontSize = 32.sp
                )
                Spacer(Modifier.weight(1f))
            }
            Row(modifier = h16dp) {
                Spacer(Modifier.weight(1f))
                val onClick = {
                    refreshed = true
                    clickHandler(RefreshCredentialsClick)
                }
                Button(onClick = onClick, enabled = !refreshed) { Text("Refresh Credentials") }
            }
            if (refreshed) Row(modifier = h16dp) {
                Spacer(Modifier.weight(1f))
                Button(
                    enabled = !refreshing,
                    onClick = { clickHandler(AttemptReloadClick) }
                ) {
                    if (refreshing) Text("Refreshing")
                    else Text("Attempt reload")
                }
            }
        }
    }
}
