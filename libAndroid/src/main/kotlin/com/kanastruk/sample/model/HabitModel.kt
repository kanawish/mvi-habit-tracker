package com.kanastruk.sample.model

import com.kanastruk.fb.rest.auth.Credentials
import com.kanastruk.fb.rest.HabitApi
import com.kanastruk.mvi.intent.Intent
import com.kanastruk.sample.android.LibAndroid
import com.kanastruk.sample.common.data.Entry
import com.kanastruk.sample.common.data.Habit
import com.kanastruk.sample.model.CacheState.*
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber


/**
 * HabitModel
 *
 * API:
 * - Load 'all' from server
 * - Offers CReate/Update/Deletion of Habits/Entries on server
 * - Local memory cache of user Habits and Entries
 * - Applies successful operations in local memory cache
 *
 * Side causes:
 * - Depends on authModel's Credentials for Retrofit 'habitApi'.
 *
 * Events:
 * - Errors on retrofit calls are exposed on `eventErrors` SharedFlow.
 *
 * This is about 'as complex' a Model example I can think of.
 * My strategy is to split things up beyond this point.
 *
 */
class HabitModel(
    authModel: AuthModel,
    val habitApiBuilder:(authToken:String)->HabitApi,
    private val scope: CoroutineScope
) {
    data class InternalState(
        val credentials: Credentials,
        val habitApi: HabitApi
    )

    private val _habitsStore = MutableStateFlow(HabitState(cacheState = LOADING))
    val habitsStore = _habitsStore.asStateFlow()

    // Errors are events, and not state, per say.
    private val _errors = MutableSharedFlow<HabitError>()
    val errors = _errors.asSharedFlow()

    // We need a new Client/Api instance any time the authToken changes.
    private val currentHabitApi = MutableStateFlow<InternalState?>(null)

    private val apiFlowJob: Job

    init {
        val apiFlow = authModel.authStore
            .map { authState ->
                when(authState) {
                    is AuthState.Ready -> authState.credentials
                    else -> null
                }
            }
            .map { nullableCreds -> nullableCreds?.let { creds ->
                InternalState(creds, habitApiBuilder(creds.idToken))
            } }

        // We need to cancel this at 'end of life' for the Model.
        apiFlowJob = apiFlow
            .onEach { currentHabitApi.value = it }
            .onCompletion { Timber.d("apiFlowJob completed $it") }
            .launchIn(scope)

        // When the api and credentials are available, execute the first reload call.
        scope.launch {
            currentHabitApi
                .filterNotNull()
                .first()
                .let { (credentials,api) ->
                    val userId = credentials.localId
                    scope.launch {
                        api.reloadProcess(userId)
                    }
                }
        }
    }

    /**
     * This is the simplest Intent processor.
     */
    private fun process(intent: Intent<HabitState>) {
        _habitsStore.value = intent.reduce(_habitsStore.value)
    }

    /**
     * We're 'baby proofing' here, by soft failing if check fails.
     *
     * Another valid approach is to be 'strict' and throw exceptions at the slightest
     * provocation. In that case, the expectation is the dev team catches and fixes issues
     * before release.
     *
     * Baby proofing with a good error reporting framework is a more customer friendly
     * approach IMO.
     *
     * @param predicate Check that current state is valid for the intent we want to process.
     * @param intent The Intent function, exposes read-only InternalState.
     */
    private fun authedProcess(
        predicate: (HabitState) -> Boolean,
        intent: (InternalState, HabitState) -> HabitState
    ) {
        val currentHabitState = _habitsStore.value
        currentHabitApi.value?.let { internalState ->
            if (predicate(currentHabitState)) intent(internalState, currentHabitState)
            else Timber.w("authedProcess() call ignored: predicate returned `false` for $currentHabitState.")
        } ?: Timber.w("authedProcess() call ignored: internal state is null.")
    }

    // A good predicate example:
    private fun isReloadable(state: HabitState):Boolean = when(state.cacheState) {
        MODIFIED, FAILED, LOADED -> true
        else -> false
    }


    // ***** PUBLIC PROCESS-INTENT FUNCTIONS *****

    /**
     * Process a 'reload' Intent, typically for pull-to-refresh
     *
     * Valid call from MODIFIED, FAILED, LOADED
     */
    fun processReloadIntent() {
        authedProcess(::isReloadable) { (credentials, api),oldState->
            val userId = credentials.localId
            scope.launch(CoroutineName("HabitModel.processReloadIntent")) { api.reloadProcess(userId) }

            oldState.copy(cacheState = LOADING).touch()
        }
    }

    /**
     * Process an 'add entry' intent. In our app, typically would be user
     * tapping on a habit to record activity, entering a quantity
     */

    /**
     * We'll give the users the ability to 'undo' the latest habit entry for a given habit.
     * I imagine either a button in the 'habit detail view' or an undo button hidden behind
     * a horizontal sliding row.
     */

    /**
     * I imagine this as an Intent that gets called upon when user presses
     * 'Save' on a habit creation form screen.
     *
     * That screen would likely require a Model of it's own, with an in-memory
     * copy of an edited habit, and a potentially short, ephemeral lifecycle.
     */

    /**
     * The creation/edit screen will be one and the same, and we'll adjust the
     * associated model to have an awareness of it being an edit or creation flow.
     *
     * Probably a nullable habitId in that Model will be sufficient to convey the
     * idea.
     *
     * Error handling will need to be built out with that in mind.
     */

    /**
     * Archiving an intent, will be a soft-delete operation. Helps keep things simple,
     * we can delegate the cleanup to the server side. (Think a 30-day deletion cycle)
     *
     * Lets us keep things really simple code wise. Also leaves the door open to
     * un-archive certain items as a bonus feature.
     *
     * re: dual param use, many ways of wrapping up the two in a type.
     * Exercise left to readers, just remember, software is never 'done'.
     *
     * see https://www.amusingplanet.com/2017/08/the-art-of-deliberate-imperfection.html
     */

    // ***** PRIVATE RETROFIT/API DEPENDENT FUNCTIONS *****
    /**
     * Must be 'LOADING' while waiting on callback.
     */
    private suspend fun HabitApi.reloadProcess(userId: String) {
        val habits = getHabits(userId).body()
        val entries = getEntries(userId).body()
        if (habits != null && entries != null) {
            process { old ->
                old.copy(
                    cacheState = LOADED,
                    habits = habits,
                    entries = entries
                ).touch()
            }
        }
    }

    /**
     * POST a new habit, updates the local cache and marks it as MODIFIED.
     */

    /**
     * PUT on an existing habit, updates the local cache and marks it as MODIFIED.
     */

    /**
     * Adds the given entry on the server, and updates the in-memory cache.
     */

    // ***** PRIVATE STATE MANIPULATION FUNCTIONS *****

    /**
     * Create a copy of HabitState with a new Entry added.
     *
     * Manipulating maps of maps gets messy. The functions below keeps everything
     * immutable by using Map's operator function, and keeps things tidy
     * in our Intent code.
     */

    /**
     * Create a copy of HabitState with the entry keyed by entryId removed from
     * habitId map of entries.
     *
     * Useful for removing 'latest entry' from the stack.
     */

    /**
     * Call this as appropriate for your Lifecycle needs.
     */
    fun destroy() {
        apiFlowJob.cancel()
    }

}

/**
 * TODO HabitApi builder function
 */
