package com.kanastruk.sample.common.data

/**
 * Entry is very simple, a value and a timestamp.
 * @property timestamp epochSeconds
 * @property value takes different meaning depending on associated habit.
 */
data class Entry(
    val timestamp:Long,
    val value:Int
)
