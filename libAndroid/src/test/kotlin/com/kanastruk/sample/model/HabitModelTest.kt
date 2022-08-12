@file:OptIn(ExperimentalCoroutinesApi::class)

package com.kanastruk.sample.model

import com.kanastruk.fb.rest.auth.Credentials
import com.kanastruk.sample.common.data.Frequency
import com.kanastruk.sample.common.data.Habit
import com.kanastruk.sample.common.data.UnitType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestResult
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import timber.log.Timber
import kotlin.test.assertNotNull

class HabitModelTest {
    companion object {
    }

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }


    @Test
    fun processReloadIntent() {
    }

    @Test
    fun processAddEntryIntent() {
    }

    @Test
    fun processUndoEntryIntent() {
    }

    @Test
    fun processCreateHabitIntent() {
    }

    @Test
    fun processUpdateHabitIntent() {
    }

    @Test
    fun processArchiveHabitIntent() {
    }

}