package com.kanastruk.sample.habit.ui

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import com.kanastruk.fb.rest.auth.Credentials
import com.kanastruk.sample.android.LibAndroid
import com.kanastruk.sample.common.data.Entry
import com.kanastruk.sample.common.data.Frequency
import com.kanastruk.sample.common.data.Habit
import com.kanastruk.sample.common.data.UnitType
import com.kanastruk.sample.model.CacheState
import com.kanastruk.sample.model.HabitState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.datetime.Clock

@Composable
fun ifLightElse(choices: Pair<Color, Color>): Color {
    return if (MaterialTheme.colors.isLight) choices.first else choices.second
}

@Composable
fun buildPreviewHabitState(): State<HabitState> {
    val previewHabit = Habit("Fake Habit", Frequency.Daily, UnitType.Weight, 10, null, null)
    val previewEntry = Entry(Clock.System.now().epochSeconds, 2)
    val previewState = HabitState(cacheState = CacheState.LOADING).run {
        val entries1 = mapOf("A" to previewEntry, "B" to previewEntry)
        val entries2 = entries1 + ("C" to previewEntry)
        val entries3 = entries2 + ("D" to previewEntry)
        copy(
            habits = mapOf("1" to previewHabit, "2" to previewHabit, "3" to previewHabit),
            entries = mapOf("1" to entries1, "2" to entries2, "3" to entries3)
        )
    }
    return MutableStateFlow(previewState).collectAsState()
}


/**
 * Nothing fancy, a quick and dirty way to store credentials.
 * Uses shared pref security model, so 'safe enough' for our needs.
 */
fun SharedPreferences.getCredentials(): Credentials? {
    val idToken = getString(LibAndroid.PrefKey.ID_TOKEN.name, null)
    val localId = getString(LibAndroid.PrefKey.LOCAL_ID.name, null)
    val refreshToken = getString(LibAndroid.PrefKey.REFRESH_TOKEN.name, null)
    return if (idToken != null && localId != null && refreshToken != null) {
        Credentials(idToken, localId, refreshToken)
    } else null
}

fun SharedPreferences.setCredentials(credentials: Credentials) {
    with(edit()) {
        putString(LibAndroid.PrefKey.ID_TOKEN.name,credentials.idToken)
        putString(LibAndroid.PrefKey.LOCAL_ID.name,credentials.localId)
        putString(LibAndroid.PrefKey.REFRESH_TOKEN.name,credentials.refreshToken)
        apply()
    }
}

