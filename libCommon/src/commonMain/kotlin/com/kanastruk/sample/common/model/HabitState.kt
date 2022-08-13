package com.kanastruk.sample.common.model

import com.kanastruk.sample.common.data.Entry
import com.kanastruk.sample.common.data.Habit
import com.kanastruk.sample.common.data.toLocalDateTime
import kotlinx.datetime.*

/**
 * HabitState holds all the user's habits and entries. Conceptually
 * you can think of it as a simple in-memory cache for server data.
 *
 * It is a 'counterpoint' to the sealed-class approach.
 * But, if you squint, you can see the same principles are at work.
 * (More obvious in the Model, how we'll try to use the
 * 'expected state' principle).
 *
 * @param touchTime in epoch milliseconds, unlike most entries/habits timestamps in epoch seconds
 * TODO: Write some tools to better 'parse' the raw maps in HabitState.
 *
 * @startuml
 * !theme k2 from /Users/kanawish/workspace/puml
 * skinparam BackgroundColor transparent
 * hide methods
 * class HabitState {
 *  + habits:Map<String,Habit>
 *  + entries:Map<String,Map<String,Entry>>
 *  + cacheState:CacheState
 * }
 * @enduml
 *
 */
data class HabitState(
    val habits: Map<String, Habit> = emptyMap(),
    val entries: Map<String, Map<String, Entry>> = emptyMap(),
    val cacheState: CacheState,
    val touchTime: Long = Clock.System.now().toEpochMilliseconds()
)

/**
 * Entry sum per habit for given date.
 */
fun HabitState.sumHabitEntries(
    timeZone: TimeZone = TimeZone.currentSystemDefault(),
    date: LocalDate = Clock.System.now().toLocalDateTime(timeZone).date
): List<Triple<String, Habit, Float>> {
    return habits
        .map { (key, habit) ->
            val todaySum = entries[key]?.values
                ?.filter { entry -> entry.toLocalDateTime(timeZone) == date }
                ?.sumOf { entry -> entry.value }
            Triple(key, habit, (todaySum?.toFloat() ?: 0f))
        }
}

/** HabitState.touch() Updates 'touchTime' to Clock 'now()'. */
fun HabitState.touch() = this.copy(touchTime = Clock.System.now().toEpochMilliseconds())

/**
 * HabitError are 'one time signals' the UX can react to, to prompt users with
 * a call to action (click here to refresh your session), or check their
 * connection, etc.
 */
sealed class HabitError {
    object AuthTokenExpired: HabitError()
    data class GenericError(val msg:String): HabitError()
}

/**
 * CacheState indicates if the cache is fresh-off-the-server, or has been modified.
 * It also lets the UI know if we're busy LOADING fresh data, or if we've hit a snag.
 *
 * @startuml
 * !theme k2 from /Users/kanawish/workspace/puml
 * skinparam BackgroundColor white
 * skinparam BackgroundColor transparent
 * skinparam StateBackgroundColor #EFEFEF
 * skinparam NoteBackgroundColor #FFFFEF
 * skinparam ArrowColor #000
 * skinparam ArrowFontColor #000
 * skinparam ArrowColor #FFF
 * skinparam ArrowFontColor #FFF
 *
 * LOADING:
 * [*] -> LOADING : init { }
 * LOADING --> LOADED
 * LOADED --> LOADING : reload()
 * MODIFIED -up-> LOADING : reload()
 * LOADING --> FAILED
 * FAILED --> LOADING : reload()
 *
 * LOADED-->BUSY: crud()
 * MODIFIED->BUSY: crud()
 * BUSY-->MODIFIED: updateCache()
 * BUSY-right->FAILED: failure()
 * note left of BUSY
 * Retrofit call
 * is 'in flight'
 * end note
 * @enduml
 *
 * Quick general idea UX wise:
 *
 * LOADING: gray screen/overlay + spinner
 * LOADED, MODIFIED: interactive list, add entry, add/edit habit, etc.
 * FAILED: Non-interactive list, refresh possible. Affordance for session management.
 *
 * See 'connectionErrors` SharedFlow for user prompts to refresh or create new anonymous account.
 *
 * NOTE: We could use a locally cached copy to re-build from scratch, as a PoC.
 */
enum class CacheState {
    LOADING, LOADED, BUSY, MODIFIED, FAILED
}
