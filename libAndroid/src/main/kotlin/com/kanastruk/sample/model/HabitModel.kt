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

/**
 * HabitApi builder function
 */
