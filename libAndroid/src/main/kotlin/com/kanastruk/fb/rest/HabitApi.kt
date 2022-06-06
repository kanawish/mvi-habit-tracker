package com.kanastruk.fb.rest

import com.kanastruk.sample.common.data.Entry
import com.kanastruk.sample.common.data.Habit
import retrofit2.Response
import retrofit2.http.*

/**
 * HabitApi gives us:
 * - Read Habits and Entries from the database.
 * - General CRuD on Habits and Entries
 *
 * The access to this data is secured per-session/anonymous user.
 * Losing your credentials means your data is lost.
 *
 * NOTE: The mock backend will be regularly reset.
 */
