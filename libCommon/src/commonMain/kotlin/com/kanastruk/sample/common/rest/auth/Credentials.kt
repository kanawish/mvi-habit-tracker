package com.kanastruk.sample.common.rest.auth

/**
 * Credentials to pass to our backend when making HabitApi REST calls.
 *
 * @param idToken aka session token.
 * @param localId Firebase user ID.
 * @param refreshToken used to refresh expired session token.
 */
data class Credentials(
    val idToken: String,
    val localId: String,
    val refreshToken: String
)
