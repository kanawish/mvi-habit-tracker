package com.kanastruk.sample.habit.ui

import kotlinx.datetime.DayOfWeek

sealed class HabitsViewEvent {
    data class WeekdayClick(val dayOfWeek: DayOfWeek):HabitsViewEvent()
    object CreateHabit:HabitsViewEvent()
    data class EditHabit(val habitKey:String):HabitsViewEvent()
    data class AddHabitEntry(val habitKey:String):HabitsViewEvent()
    data class UndoHabitEntry(val habitKey:String):HabitsViewEvent()
    object CalendarMode:HabitsViewEvent()
    object Camera:HabitsViewEvent()
    object Timer:HabitsViewEvent()
    object Settings:HabitsViewEvent()
}