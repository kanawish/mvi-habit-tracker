package com.kanastruk.sample.common.rest.auth

import kotlinx.serialization.Serializable

/**
 * Response we get from the IdentityToolkitApi signupAnonymous() call,
 * with utility function to convert into 'Credentials'
 */
@Serializable
data class AnonymousResponse(
    val expiresIn: String,
    val idToken: String,
    val kind: String,
    val localId: String,
    val refreshToken: String
) {
    fun toCredentials() = Credentials(
        idToken,
        localId,
        refreshToken
    )
}


