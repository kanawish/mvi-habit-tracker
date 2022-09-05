package com.kanastruk.sample.habit.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CalendarViewDay
import androidx.compose.material.icons.filled.CalendarViewWeek
import androidx.compose.material.icons.filled.Colorize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kanastruk.sample.common.data.Frequency.Daily
import com.kanastruk.sample.common.data.Frequency.Weekly
import com.kanastruk.sample.common.data.Habit
import com.kanastruk.sample.common.data.UnitType.*
import com.kanastruk.sample.habit.R
import com.kanastruk.sample.habit.ui.HabitEditorViewEvent.*
import com.kanastruk.sample.habit.ui.theme.MyApplicationTheme
import com.kanastruk.sample.habit.ui.theme.SpartanFamily
import com.kanastruk.sample.habit.ui.theme.colorOptions
import kotlinx.datetime.Clock
import timber.log.Timber

@Preview(
    name = "Edit Habit Screen", device = "spec:shape=Normal,width=411,height=600,unit=dp,dpi=420",
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun PreviewHabitEditorCreateView() {
    MyApplicationTheme {
        HabitEditorView(
            initialHabit = Habit("New Habit", Daily, Todo, 1, Clock.System.now().epochSeconds, null)
        ) {
            Timber.d("✨ ViewEvent handler called with $it")
        }
    }
}

@Preview(
    name = "Edit Habit Screen", device = "spec:shape=Normal,width=411,height=600,unit=dp,dpi=420",
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Preview(
    name = "Edit Habit Screen", device = "spec:shape=Normal,width=411,height=600,unit=dp,dpi=420",
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun PreviewHabitEditorView() {
    val previewEditedHabit = Habit("Fake Habit", Daily, Weight, 10, null, null)
    MyApplicationTheme {
        HabitEditorView("42", previewEditedHabit) {
            Timber.d("💾️ ViewEvent handler called with $it")
        }
    }
}

/**
 * Create or update a habit.
 * @param habitId create mode when null
 * @param initialHabit source habit
 */
@Composable
fun HabitEditorView(
    habitId: String? = null,
    initialHabit: Habit,
    handler: (HabitEditorViewEvent) -> Unit
) {
    LogCompositions(tag = "HabitsEditor", msg = "Recomposed with $initialHabit")

    // TODO: https://developer.android.com/reference/kotlin/androidx/compose/foundation/layout/Arrangement
    Scaffold {
        Column(modifier = Modifier.padding(vertical = 16.dp)) {
            var edited by remember { mutableStateOf(initialHabit) }
            Row(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = habitId?.let { "Edit Habit" } ?: "Create Habit",
                    fontFamily = SpartanFamily,
                    fontWeight = FontWeight.Black,
                    fontSize = 32.sp
                )
                Spacer(Modifier.weight(1f))
            }
            Spacer(Modifier.size(8.dp))

            TextField(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                value = edited.name,
                singleLine = true,
                onValueChange = { string ->
                    edited = edited.copy(name = string.replace("\n", ""))
                },
                label = { Text("Name") })
            Spacer(Modifier.size(8.dp))

            // FREQUENCY
            DropDownPicker(
                label = "Frequency",
                initialPick = edited.frequency.toString(),
                options = listOf(
                    IconOption(
                        Weekly.toString(),
                        { edited = edited.copy(frequency = Weekly) }
                    ) { Icon(Icons.Default.CalendarViewWeek, null) },
                    IconOption(
                        Daily.toString(),
                        { edited = edited.copy(frequency = Daily) }
                    ) { Icon(Icons.Default.CalendarViewDay, null) }
                ),
                leadingIcon = ImageVector.vectorResource(R.drawable.ic_baseline_calendar_month_24)
            )
            Spacer(Modifier.size(8.dp))

            // TYPE
            DropDownPicker(
                label = "Type",
                initialPick = edited.unit.toString(),
                options = values()
                    .map { unitType ->
                        IconOption(
                            unitType.toString(),
                            { edited = edited.copy(unit = unitType) }
                        )
                    },
                leadingIcon = Icons.Default.Calculate
            )
            Spacer(Modifier.size(8.dp))

            // GOAL
            TextField(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                value = edited.goal.toString(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = { string ->
                    Timber.d("goal: \"$string\"->${string.toIntOrNull()}")
                    edited = edited.copy(goal = string.toIntOrNull() ?: 0)
                },
                label = { Text("Goal") })
            Spacer(Modifier.size(8.dp))

            if( false ) {
                DropDownPicker(
                    label = "Color",
                    initialPick = colorOptions[0].label,
                    options = colorOptions.map { it.toIconOption() },
                    leadingIcon = Icons.Default.Colorize
                )
                Spacer(Modifier.size(8.dp))
            }

            // BUTTONS
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(onClick = { handler(Cancel) }) {
                    Text("Cancel")
                }
                Spacer(Modifier.size(8.dp))

                val focusManager = LocalFocusManager.current
                val onClick = {
                    focusManager.clearFocus()
                    val event = when (habitId) {
                        null -> Create(edited)
                        else -> Save(habitId, edited)
                    }
                    handler(event)
                }
                Button(onClick = onClick) {
                    when (habitId) {
                        null -> Text("Create")
                        else -> Text("Save")
                    }
                }
            }

        }
    }
}