@file:OptIn(ExperimentalMaterialApi::class)

package com.kanastruk.sample.habit.ui

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.material.DismissValue.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.ListAlt
import androidx.compose.material.icons.twotone.Camera
import androidx.compose.material.icons.twotone.EditCalendar
import androidx.compose.material.icons.twotone.Settings
import androidx.compose.material.icons.twotone.Timer
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kanastruk.sample.common.data.Entry
import com.kanastruk.sample.common.data.Frequency
import com.kanastruk.sample.common.data.Habit
import com.kanastruk.sample.common.data.UnitType
import com.kanastruk.sample.habit.R
import com.kanastruk.sample.habit.ui.HabitsViewEvent.*
import com.kanastruk.sample.habit.ui.theme.MyApplicationTheme
import com.kanastruk.sample.habit.ui.theme.SpartanFamily
import com.kanastruk.sample.habit.ui.theme.colorOptions
import com.kanastruk.sample.model.CacheState
import com.kanastruk.sample.model.HabitState
import com.kanastruk.sample.model.sumHabitEntries
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.datetime.*
import timber.log.Timber
import kotlin.math.roundToInt

/**
 * Food for recomposition thinking:
 *
 * https://github.com/androidx/androidx/blob/androidx-main/compose/compiler/design/compiler-metrics.md
 * https://www.jetpackcompose.app/articles/how-can-I-debug-recompositions-in-jetpack-compose
 * https://www.jetpackcompose.app/articles/donut-hole-skipping-in-jetpack-compose
 * https://chris.banes.dev/composable-metrics/
 *
 * Some key points:
 * - Lists are unstable. Fix with an @Immutable + data class wrapper.
 */



@Preview(
    name = "Main Habit Screen",
    device = "spec:shape=Normal,width=411,height=600,unit=dp,dpi=420",
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Preview(
    name = "Main Habit Screen",
    device = "spec:shape=Normal,width=411,height=600,unit=dp,dpi=420",
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun PreviewHabitsView() {
    MyApplicationTheme {
        val habitState = buildPreviewHabitState()
        HabitsView(habitState.value) { Timber.d("📊 ViewEvent handler called with $it") }
    }
}

/**
 *  Top level composable view functions that acts as a 'state holder'.
 */
@Composable
fun HabitsView(habitState: HabitState, handler: (HabitsViewEvent) -> Unit) {
    LogCompositions(tag = "HabitsView", msg = "Recomposed $habitState")

    // Internal UI state, editing or no? Toggled from FAB, impacts main list of habits view.
    var editingMode by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = { WeeklyTopView(handler) },
        bottomBar = { HabitsBottomAppBar(handler) },
        floatingActionButton = {
            FloatingActionButton(onClick = { editingMode = !editingMode }) {
                if( !editingMode ) Icon(Icons.Default.Edit, null)
                else Icon(Icons.Outlined.ListAlt, null)
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true
    ) {
        LazyColumn(Modifier.padding(vertical = 16.dp)) {
            val summedHabits = habitState.sumHabitEntries()
            summedHabits.forEachIndexed { index, (habitId,habit,todaySum) ->
                item(key = habitId) {
                    val rowModifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 4.dp)
                    Row(rowModifier) {
                        val boxModifier = Modifier
                            .weight(1f)
                            .height(48.dp)

                        val progressRatio = todaySum / habit.goal
                        val progressDescription = if (!progressRatio.isNaN()) "${(progressRatio * 100).roundToInt()}%"
                        else "Error: Goal probably set to 0."

                        val habitColor = colorOptions[index % colorOptions.size].color()
                        val dismissHandler: (DismissValue) -> Unit = { dv ->
                            if (dv == DismissedToEnd) handler(AddHabitEntry(habitId))
                            else if( dv == DismissedToStart ) handler(UndoHabitEntry(habitId))
                        }
                        if (editingMode) HabitProgressBar(
                            boxModifier = boxModifier,
                            habitName = habit.name,
                            progress = progressRatio,
                            progressDescription = progressDescription,
                            color = habitColor
                        ) else SwipeToEdit(boxModifier, dismissHandler) {
                            HabitProgressBar(
                                habitName = habit.name,
                                progress = progressRatio,
                                progressDescription = progressDescription,
                                color = habitColor
                            )
                        }
                        if( editingMode ) OutlinedButton(
                            modifier = Modifier
                                .padding(start = 4.dp)
                                .size(48.dp),
                            onClick = { handler(EditHabit(habitId)) }
                        ) { Icon(Icons.Filled.Edit, contentDescription = null) }
                    }
                }
            }

            // "Add" as last item while in edit mode.
            if( editingMode ) item {
                Row(modifier = Modifier.padding(horizontal = 16.dp)) {
                    OutlinedButton(
                        modifier = Modifier
                            .height(48.dp)
                            .weight(1f)
                            .padding(end = (4 + 48).dp),
                        onClick = { handler(CreateHabit) }
                    ) { Text("New Habit") }
                }
            }
        }
    }
}

/**
 */
@Composable
fun SwipeToEdit(modifier:Modifier, handler: (DismissValue) -> Unit, dismissContent: @Composable RowScope.() -> Unit) {
    val dismissState = rememberDismissState(
        confirmStateChange = {
            Timber.d("DismissValue = $it")
            handler(it)
            false
        }
    )
    SwipeToDismiss(
        modifier = modifier,
        state = dismissState,
        directions = setOf(DismissDirection.StartToEnd, DismissDirection.EndToStart),
        dismissThresholds = { FractionalThreshold(0.15f) },
        background = {
            val direction = dismissState.dismissDirection ?: return@SwipeToDismiss
            val color by animateColorAsState(
                when (dismissState.targetValue) {
                    Default -> Color.LightGray
                    DismissedToEnd -> Color.Green
                    DismissedToStart -> Color.Red
                }
            )
            val alignment = when (direction) {
                DismissDirection.StartToEnd -> Alignment.CenterStart
                DismissDirection.EndToStart -> Alignment.CenterEnd
            }
            val icon = when (direction) {
                DismissDirection.StartToEnd -> Icons.Default.Done
                DismissDirection.EndToStart -> Icons.Default.Remove
            }
            val scale by animateFloatAsState(
                if (dismissState.targetValue == Default) 0.75f else 1f
            )
            Box(
                Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(horizontal = 20.dp),
                contentAlignment = alignment
            ) {
                Icon(
                    icon,
                    contentDescription = "Localized description",
                    modifier = Modifier.scale(scale)
                )
            }
        },
        dismissContent = dismissContent
    )
}

@Composable
private fun HabitsBottomAppBar(handler: (HabitsViewEvent) -> Unit) {
    BottomAppBar(
        cutoutShape = MaterialTheme.shapes.small.copy(CornerSize(percent = 50))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { handler(CalendarMode) }) {
                Icon(Icons.TwoTone.EditCalendar, null, Modifier.size(32.dp))
            }
            IconButton(onClick = { handler(Camera) }) {
                Icon(Icons.TwoTone.Camera, null, Modifier.size(32.dp))
            }
            Spacer(modifier = Modifier.size(56.dp))
            IconButton(onClick = { handler(Timer) }) {
                Icon(Icons.TwoTone.Timer, null, Modifier.size(32.dp))
            }
            IconButton(onClick = { handler(Settings) }) {
                Icon(Icons.TwoTone.Settings, null, Modifier.size(32.dp))
            }
        }
    }
}

/**
 */
@Composable
private fun WeeklyTopView(handler: (HabitsViewEvent) -> Unit) {
    LogCompositions(tag = "WeeklyTopView", msg = "Recomposed")
    TopAppBar(modifier = Modifier.height(106.dp)) { //
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .height(80.dp)
                .padding(bottom = 16.dp, top = 8.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                //
                Text(
                    text = stringResource(R.string.habitTrackerTitle),
                    fontFamily = SpartanFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    color = ifLightElse(MaterialTheme.colors.onPrimary to MaterialTheme.colors.onSurface)
                )
            }
            Spacer(modifier = Modifier.size(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // A bit gross but 🤷‍ good enough for our sample.
                DayOfWeek.values().forEach {
                    LetterButton(it.name.first()) { handler(WeekdayClick(it)) }
                }
            }
        }
    }
}