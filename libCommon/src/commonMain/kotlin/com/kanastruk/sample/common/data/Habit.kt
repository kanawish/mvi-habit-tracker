package com.kanastruk.sample.common.data

data class Habit(
    val name: String,
    val frequency: Frequency,
    val unit: UnitType,
    val goal: Int,
    val startDate: Long?,
    val endDate: Long?,
    val archived: Boolean = false
)

enum class UnitType {
    Counter, Todo, Liquid, Weight
}

enum class Frequency {
    Weekly, Daily
}
