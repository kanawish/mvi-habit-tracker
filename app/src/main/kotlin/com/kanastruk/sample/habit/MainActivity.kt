package com.kanastruk.sample.habit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kanastruk.sample.common.data.Frequency
import com.kanastruk.sample.common.data.Habit
import com.kanastruk.sample.common.data.UnitType
import com.kanastruk.sample.habit.MainActivity.Nav.*
import com.kanastruk.sample.habit.ui.*
import com.kanastruk.sample.habit.ui.theme.MyApplicationTheme
import com.kanastruk.sample.model.AuthModel
import com.kanastruk.sample.model.HabitModel
import com.kanastruk.sample.model.HabitState
import org.koin.android.ext.android.inject
import timber.log.Timber

class MainActivity : ComponentActivity() {
    private val authModel: AuthModel by inject()
    private val habitModel:HabitModel by inject()

    enum class Nav { HABITS, CREATE, EDIT }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                val authState by authModel.authStore.collectAsState()
                // val habitState by habitModel.habitsStore.collectAsState()
                val habitState: State<HabitState> = habitModel.habitsStore.collectAsState()
                LogCompositions(tag = "MainActivity/habitState", msg = "habitStore: ${habitModel.habitsStore.value}")

                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = HABITS.name) {
                    composable(HABITS.name) {
                        HabitsView(habitState.value) { event ->
                            Timber.d("ViewEvent handler called with $event")
                            when (event) {
                                HabitsViewEvent.CreateHabit -> navController.navigate(CREATE.name)
                                is HabitsViewEvent.EditHabit -> navController.navigate(EDIT.name)
                                else -> Timber.d("🤷‍♂️")
/*
                                is HabitsViewEvent.AddHabitEntry -> TODO()
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
                    val editorEventHandler: (HabitEditorViewEvent) -> Unit = { event ->
                        Timber.d("✍️️ ViewEvent handler called with $event")
                        when (event) {
                            HabitEditorViewEvent.Cancel -> navController.navigate(HABITS.name)
                            is HabitEditorViewEvent.Create -> navController.navigate(HABITS.name)
                            is HabitEditorViewEvent.Save -> navController.navigate(HABITS.name)
                        }
                    }
                    composable(CREATE.name) {
                        val previewEditedHabit = Habit("Fake Habit", Frequency.Daily, UnitType.Weight, 10, null, null)
                        HabitEditorView("42", previewEditedHabit, editorEventHandler)
                    }
                    composable(EDIT.name) {
                        val previewEditedHabit = Habit("Fake Habit", Frequency.Daily, UnitType.Weight, 10, null, null)
                        HabitEditorView(handler = editorEventHandler)
                    }
                }
            }
        }
    }
}
