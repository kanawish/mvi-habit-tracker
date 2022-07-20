package com.kanastruk.sample.habit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kanastruk.sample.common.data.Frequency
import com.kanastruk.sample.common.data.Habit
import com.kanastruk.sample.common.data.UnitType
import com.kanastruk.sample.habit.MainActivity.Arg.asPathArg
import com.kanastruk.sample.habit.MainActivity.Nav.*
import com.kanastruk.sample.habit.ui.*
import com.kanastruk.sample.habit.ui.HabitsViewEvent.*
import com.kanastruk.sample.habit.ui.theme.MyApplicationTheme
import com.kanastruk.sample.model.AuthModel
import com.kanastruk.sample.model.HabitModel
import kotlinx.datetime.Clock
import org.koin.android.ext.android.inject
import timber.log.Timber

class MainActivity : ComponentActivity() {
    private val authModel: AuthModel by inject()
    private val habitModel: HabitModel by inject()

    object Arg {
        const val habitId = "habitId"
        fun String.asPathArg():String = "/{${this}}"
    }

    enum class Nav(val path:String) {
        // TODO: Fix my nav key scheme
        HABITS("habits"), CREATE("create"), EDIT("edit");
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyApplicationTheme {
                val authState by authModel.authStore.collectAsState()
                val habitState by habitModel.habitsStore.collectAsState()
                LogCompositions(tag = "MainActivity/habitState", msg = "habitStore: ${habitModel.habitsStore.value}")

                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = HABITS.path) {
                    composable(HABITS.path) {
                        AuthWrapper(authState = authState, habitState = habitState, handler = ::authHandler) {
                            HabitsView(habitState) { event ->
                                Timber.d("ViewEvent handler called with $event")
                                when (event) {
                                    CreateHabit -> navController.navigate(CREATE.path)
                                    is EditHabit -> navController.navigate("${EDIT.path}/${event.habitKey}")
                                    is AddHabitEntry -> habitModel.processAddEntryIntent(event.habitKey, 1)
                                    is UndoHabitEntry -> habitModel.processUndoEntryIntent(event.habitKey)
                                    else -> Timber.d("We ignore those for now. 🤷‍♂️")
/*
                                HabitsViewEvent.CalendarMode -> TODO()
                                HabitsViewEvent.Camera -> TODO()
                                HabitsViewEvent.Settings -> TODO()
                                HabitsViewEvent.Timer -> TODO()
                                is HabitsViewEvent.UndoHabitEntry -> TODO()
                                is HabitsViewEvent.WeekdayClick -> TODO()
*/
                                }
                            }
                        }
                    }
                    val editorEventHandler: (HabitEditorViewEvent) -> Unit = { event ->
                        Timber.d("✍️️ ViewEvent handler called with $event")
                        when (event) {
                            HabitEditorViewEvent.Cancel -> navController.navigate(HABITS.path)
                            is HabitEditorViewEvent.Create -> {
                                habitModel.processCreateHabitIntent(event.habit)
                                navController.navigate(HABITS.path)
                            }
                            is HabitEditorViewEvent.Save -> {
                                habitModel.processUpdateHabitIntent(event.id, event.habit)
                                navController.navigate(HABITS.path)
                            }
                        }
                    }
                    composable(CREATE.path) {
                        AuthWrapper(authState = authState, habitState = habitState, handler = ::authHandler) {
                            HabitEditorView(null, createNewHabit(), editorEventHandler)
                        }
                    }
                    composable(EDIT.path+Arg.habitId.asPathArg()) { entry ->
                        // NOTE: With soft-fallback to 'create'.
                        val habitId = entry.arguments?.getString(Arg.habitId)
                        val habit = habitId?.let { habitModel.getHabit(habitId) } ?: createNewHabit()

                        AuthWrapper(authState = authState, habitState = habitState, handler = ::authHandler) {
                            HabitEditorView(habitId = habitId, initialHabit = habit, handler = editorEventHandler)
                        }
                    }
                }
            }
        }
    }

    private fun createNewHabit():Habit = Habit("New Habit", Frequency.Daily, UnitType.Todo, 1, Clock.System.now().epochSeconds, null)

    private fun authHandler(event: AuthViewEvent) {
        when (event) {
            AuthViewEvent.RefreshCredentialsClick -> authModel.refreshToken() // NOTE: Rename to 'processRefreshTokenIntent()'?
            AuthViewEvent.AttemptReloadClick -> habitModel.processReloadIntent()
        }
    }
}
