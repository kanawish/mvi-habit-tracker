package com.kanastruk.sample.common.data

/**
 * Habit is fairly detailed.
 */
data class Habit(
    val name: String,
    val frequency: Frequency,
    val unit: UnitType,
    val goal: Int,
    val startDate: Long?,
    val endDate: Long?,
    val archived: Boolean = false
)

