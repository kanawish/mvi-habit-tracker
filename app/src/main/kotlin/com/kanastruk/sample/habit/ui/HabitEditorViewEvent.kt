package com.kanastruk.sample.habit.ui

import com.kanastruk.sample.common.data.Habit

sealed class HabitEditorViewEvent {
    data class Create(val habit: Habit) : HabitEditorViewEvent()
    data class Save(val id: String, val habit: Habit) : HabitEditorViewEvent()
    object Cancel : HabitEditorViewEvent()
}
