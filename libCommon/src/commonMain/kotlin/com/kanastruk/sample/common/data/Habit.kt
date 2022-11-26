package com.kanastruk.sample.common.data

/**
 * Habit is fairly detailed.
 *
 * @property startDate in epochSeconds, should be UTC 'rounded' to drop the time-component.
 * @property endDate in epochSeconds, should be UTC 'rounded' to drop the time-component.
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

