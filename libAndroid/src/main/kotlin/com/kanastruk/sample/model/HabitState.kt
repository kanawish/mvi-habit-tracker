package com.kanastruk.sample.model

import com.kanastruk.sample.common.data.Entry
import com.kanastruk.sample.common.data.Habit
import kotlinx.datetime.Clock

/**
 * HabitState holds all the user's habits and entries. Conceptually
 * you can think of it as a simple in-memory cache for server data.
 *
 * It is a 'counterpoint' to the sealed-class approach.
 * But, if you squint, you can see the same principles are at work.
 * (More obvious in the Model, how we'll try to use the
 * 'expected state' principle).
 *
 * TODO: Write some tools to better 'parse' the raw maps in HabitState.
 */

/** HabitState.touch() Updates 'touchTime' to Clock 'now()'. */

/**
 * HabitError are 'one time signals' the UX can react to, to prompt users with
 * a call to action (click here to refresh your session), or check their
 * connection, etc.
 */

/**
 * CacheState indicates if the cache is fresh-off-the-server, or has been modified.
 * It also lets the UI know if we're busy LOADING fresh data, or if we've hit a snag.
 *
 * @startuml
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
 * note right of BUSY
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
