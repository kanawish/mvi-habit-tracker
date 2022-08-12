@file:OptIn(ExperimentalCoroutinesApi::class)

package com.kanastruk.sample.model

import com.kanastruk.fb.rest.auth.Credentials
import com.kanastruk.sample.model.AuthState.*
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import timber.log.Timber
import kotlin.test.assertIs

/**
 * Reference material:
 *
 * http://d.android.com/tools/testing
 * https://proandroiddev.com/testing-retrofit-converter-with-mock-webserver-50f3e1f54013
 * https://medium.com/mobile-app-development-publication/kotlin-coroutine-scope-context-and-job-made-simple-5adf89fcfe94
 * https://github.com/Kotlin/kotlinx.coroutines/tree/master/kotlinx-coroutines-test
 * https://stackoverflow.com/questions/62110761/unit-test-the-new-kotlin-coroutine-stateflow
 * https://github.com/square/okhttp/tree/master/mockwebserver
 *
 */
class AuthModelTest {

    companion object {
    }

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    /**
     * This is an example of testing model behaviour.
     *
     * This tests the 'first use' scenario, where no credentials were loaded,
     * and AuthModel should sign the users up anonymously.
     *
     * This should transition us from INIT -to-> READY,
     * and we validate that the correct info was fetched from our MockWebServer.
     *
     * The timeout value below is for cases where the flows don't return the
     * number of expected items, and other timing issues.
     */
    @Test
    fun init() = runTest(dispatchTimeoutMs = 3000) {
    }

    @Test
    fun refreshToken() = runTest(dispatchTimeoutMs = 3000) {
    }

}

