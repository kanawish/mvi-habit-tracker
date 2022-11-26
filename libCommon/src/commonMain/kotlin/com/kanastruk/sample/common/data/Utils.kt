package com.kanastruk.sample.common.data

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun Entry.toLocalDateTime(timeZone: TimeZone = TimeZone.currentSystemDefault()): LocalDate {
    return Instant.fromEpochSeconds(timestamp).toLocalDateTime(timeZone).date
}

