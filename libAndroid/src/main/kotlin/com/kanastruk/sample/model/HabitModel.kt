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

    // TODO: Whole set of API intents (Load + CRuD of Habits and Entries)
    // NOTE: Keep those as mocks until we implement API calls.

    // ***** PRIVATE RETROFIT/API DEPENDENT FUNCTIONS *****
    /**
     * Must be in 'LOADING' while waiting on callback.
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
     * Call this as appropriate for your Lifecycle needs.
     */
    fun destroy() {
        apiFlowJob.cancel()
    }

}

/**
 * TODO HabitApi builder function
 */
