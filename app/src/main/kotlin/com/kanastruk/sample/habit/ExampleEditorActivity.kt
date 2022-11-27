package com.kanastruk.sample.habit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.kanastruk.sample.common.data.Frequency
import com.kanastruk.sample.common.data.Habit
import com.kanastruk.sample.common.data.UnitType
import com.kanastruk.sample.habit.ui.*
import com.kanastruk.sample.habit.ui.FailedStateViewEvent.*
import com.kanastruk.sample.habit.ui.theme.MyApplicationTheme
import com.kanastruk.sample.model.*
import kotlinx.datetime.Clock
import org.koin.android.ext.android.inject
import timber.log.Timber

/**
 * An example editor activity, will be our first lesson with views.
 * Next lesson will add the main habits-list screen.
 *
 * NOTE: I've been thinking about the 'formalized' approach a lot of folks are selling vs
 *  the more organic one. Especially in the MPP-MVI approach... Toolbox vs framework. I'll
 *  pick the toolbox for exploring 'fresh new vistas'. Frameworks are nice when working with
 *  known quantities, and can save time when scaling a dev team. (Some pitfalls there too.)
 *
 */
class ExampleEditorActivity : ComponentActivity() {
    private val authModel: AuthModel by inject()
    private val habitModel: HabitModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                val editorEventHandler: (HabitEditorViewEvent) -> Unit = { event ->
                    Timber.d("✍️️ ViewEvent handler called with $event")
                    when (event) {
                        HabitEditorViewEvent.Cancel -> {
                            Timber.d("Triggers reload and causes a recomposition.")
                            habitModel.processReloadIntent()
                        }
                        is HabitEditorViewEvent.Create -> {
                            Timber.d("Creates a new entry and causes recomposition.")
                            habitModel.processCreateHabitIntent(event.habit)
                        }
                        is HabitEditorViewEvent.Save -> TODO("Can't happen in this example.")
                    }
                }


                val wrappedContent: @Composable () -> Unit = { HabitEditorView(null, newClearHabit(), editorEventHandler) }

                val authState by authModel.authStore.collectAsState()
                val habitState by habitModel.habitsStore.collectAsState()
                LogCompositions(tag = "authState/habitState", msg = "authModelState: $authState\nhabitState: $habitState" )

                // NOTE: Smart cast breaks when accessing authState directly.
                // TODO: (vid: Maybe do the authState[State] 'yo' idea?)
                AuthWrapper(authState, habitState, wrappedContent)

            }
        }
    }

    @Composable
    private fun AuthWrapper(
        authState: AuthState,
        habitState: HabitState,
        wrappedContent: @Composable () -> Unit
    ) {
        when (authState) {
            AuthState.Init -> BusyStateView(label = "AUTH INITIALIZING")
            is AuthState.Refreshing,
            is AuthState.Ready -> when (habitState.cacheState) {
                CacheState.BUSY,
                CacheState.LOADING -> BusyStateView(habitState.cacheState.name)
                CacheState.FAILED -> ProposeRefresh(
                    habitState.cacheState.name,
                    authState is AuthState.Refreshing
                ) {
                    when (it) {
                        RefreshCredentialsClick -> authModel.refreshToken() // NOTE: Rename to 'processRefreshTokenIntent()'?
                        AttemptReloadClick -> habitModel.processReloadIntent()
                    }
                }
                CacheState.LOADED,
                CacheState.MODIFIED -> wrappedContent()
            }
            is AuthState.Error -> BusyStateView(label = "AUTH ERROR ${authState.msg}")
        }
    }

    private fun newClearHabit() = Habit(
        "",
        Frequency.Daily,
        UnitType.Weight,
        0,
        Clock.System.now().toEpochMilliseconds(),
        null
    )

}
