package com.kanastruk.fb.rest.auth

/**
 * Credentials to pass to our backend when making HabitApi REST calls.
 */
data class Credentials(
    val idToken: String,
    val localId: String,
    val refreshToken: String
)
