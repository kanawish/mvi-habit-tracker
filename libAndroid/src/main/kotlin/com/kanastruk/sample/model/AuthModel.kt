package com.kanastruk.sample.model

import com.kanastruk.fb.rest.auth.Credentials
import com.kanastruk.fb.rest.*
import com.kanastruk.fb.rest.auth.AnonymousResponse
import com.kanastruk.fb.rest.auth.SecureTokenApi
import com.kanastruk.mvi.intent.Intent
import com.kanastruk.mvi.intent.expectingIntent
import com.kanastruk.sample.android.LibAndroid
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

