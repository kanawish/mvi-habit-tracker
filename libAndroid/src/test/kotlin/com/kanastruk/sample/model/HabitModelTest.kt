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
        @BeforeClass @JvmStatic
        fun onlyOnce() {
            Timber.plant(object : Timber.Tree() {
                override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                    println("[T$priority${tag?.let { "|$it" } ?: ""}]:$message")
                }
            })
        }
    }

    private var mockWebServer = MockWebServer()
    private val expectedCredentials = Credentials("anIdToken", "anAnonymousUserId", "aRefreshToken")
    private val habitAId = "habitA"

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    private suspend fun StateFlow<HabitState>.first(cacheState: CacheState) =
        filter { it.cacheState == cacheState }.first()

    /**
     * We always end up waiting on an initial State to load.
     * Then we run a testing block.
     * And finally we destroy() the habitModel instance.
     */
    private fun runHabitModelTest(expectedCount: Int, block: CoroutineScope.(HabitModel) -> Unit) =
        runTest(dispatchTimeoutMs = 2500) {
            val (_, habitModel) = initialModels()
            launch {
                val initialState = habitModel.habitsStore.first(CacheState.LOADED)
                assertInitialLoaded(initialState)

                this.block(habitModel)
                habitModel.destroy()
            }
            assertExpectedTransitionCount(habitModel, expectedCount)
        }

    @Test
    fun processReloadIntent() = runHabitModelTest(4) { habitModel ->
        mockWebServer.enqueueResponse("r.habits.reload.json", 200)
        mockWebServer.enqueueResponse("r.entries.reload.json", 200)
        launch {
            habitModel.processReloadIntent()

            val reloadedState = habitModel.habitsStore.first(CacheState.LOADED)
            assertReloadLoaded(reloadedState)
        }
    }

    @Test
    fun processAddEntryIntent() = runHabitModelTest(4) { habitModel ->
        mockWebServer.enqueueResponse("r.name.entry.json", 200)
        launch {
            habitModel.processAddEntryIntent(habitAId, 42)

            val modifiedState = habitModel.habitsStore.first(CacheState.MODIFIED)
            val actual = modifiedState.entries[habitAId]?.get("newEntryUID")
            Timber.i("actual 'newEntryUID': $actual")
            assertNotNull(actual)
        }
    }

    @Test
    fun processUndoEntryIntent() = runHabitModelTest(4) { habitModel ->
        mockWebServer.enqueue(MockResponse().setResponseCode(200))
        launch {
            habitModel.processUndoEntryIntent(habitAId)

            val modifiedState = habitModel.habitsStore.first(CacheState.MODIFIED)
            val entryMap = modifiedState.entries[habitAId]
            assertEquals(1, entryMap?.size)
            assertEquals(42L, entryMap?.get("firstEntryID")?.timestamp)
        }
    }

    @Test
    fun processCreateHabitIntent() = runHabitModelTest(4) { habitModel ->
        val expectedId = "habitC"
        val newHabit = Habit("Habit C", Frequency.Daily, UnitType.Weight, 10, null, null)
        mockWebServer.enqueueResponse("r.habits.create.json", 200)

        launch {
            habitModel.processCreateHabitIntent(newHabit)
            val modifiedState = habitModel.habitsStore.first(CacheState.MODIFIED)
            assert(modifiedState.habits.containsKey(expectedId))
        }
    }

    @Test
    fun processUpdateHabitIntent() = runHabitModelTest(4) { habitModel ->
        val expectedName = "Drinking water"
        mockWebServer.enqueueResponse("r.habits.update.json", 200)
        launch {
            habitModel.habitsStore.value.habits[habitAId]?.let { habitA ->
                val updated = habitA.copy(name = expectedName)
                habitModel.processUpdateHabitIntent(habitAId, updated)
            }
            val modifiedState = habitModel.habitsStore.first(CacheState.MODIFIED)
            assert(modifiedState.habits[habitAId]?.name.equals(expectedName))
        }
    }

    @Test
    fun processArchiveHabitIntent() = runHabitModelTest(4) { habitModel ->
        mockWebServer.enqueueResponse("r.habits.archive.json", 200)
        launch {
            habitModel.processArchiveHabitIntent(habitAId)
            val modifiedState = habitModel.habitsStore.first(CacheState.MODIFIED)
            assert(modifiedState.habits[habitAId]?.archived ?: false)
        }
    }

    /**
     * Superficially assert that r.*.initial.json was loaded.
     * [We're not trying to test against GSON parser, after all.]
     */
    private fun assertInitialLoaded(state: HabitState) {
        // Assert we have 2 habits, with 2 entries, and that keying makes sense.
        assertEquals(2, state.habits.size)
        assertEquals(2, state.entries.size)
        state.habits.forEach { (key, _) ->
            val map = state.entries[key]
            assertNotNull(map)
            assertEquals(map.size, 2)
        }
        Timber.i("✅ assertInitialLoaded complete")
    }

    /**
     * Superficially assert that r.*.reload.json was loaded.
     */
    private fun assertReloadLoaded(state: HabitState) {
        // Assert we have 2 habits, with 2 entries, and that keying makes sense.
        assertEquals(1, state.habits.size)
        assertEquals(1, state.entries.size)
        state.habits.forEach { (key, _) ->
            val map = state.entries[key]
            assertNotNull(map)
            assertEquals(3, map.size)
        }
        Timber.i("✅ assertReloadLoaded complete")
    }

    private fun CoroutineScope.assertExpectedTransitionCount(
        habitModel: HabitModel,
        expectedCount: Int
    ) {
        habitModel.habitsStore
            .map { it.touchTime.toString().takeLast(5) to it.cacheState }
            .take(expectedCount)
            .onEach { Timber.i("🗃 CacheState: $it") }
            .onCompletion { Timber.i("✅ onCompletion for expectedCount: $expectedCount \uD83D\uDDC3") }
            .launchIn(this)
    }

    private fun CoroutineScope.initialModels(): Pair<AuthModel, HabitModel> {
        mockWebServer.enqueueResponse("r.habits.initial.json", 200)
        mockWebServer.enqueueResponse("r.entries.initial.json", 200)

        val authModel = buildTestAuthModel(mockWebServer.url("/").toString(), expectedCredentials)
        return authModel to buildTestHabitModel(mockWebServer.url("/").toString(), authModel)
    }

}