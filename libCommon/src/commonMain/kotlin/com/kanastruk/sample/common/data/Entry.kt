package com.kanastruk.sample.common.data

import kotlinx.serialization.Serializable

/**
 * Entry is very simple, a value and a timestamp.
 * @property timestamp epochSeconds
 * @property value takes different meaning depending on associated habit.
 */
@Serializable
data class Entry(
    val timestamp:Long,
    val value:Int
)
