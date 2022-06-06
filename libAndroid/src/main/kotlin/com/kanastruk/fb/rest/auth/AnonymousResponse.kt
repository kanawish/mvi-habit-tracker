package com.kanastruk.fb.rest.auth

/**
 * Response we get from the IdentityToolkitApi signupAnonymous() call,
 * with utility function to convert into 'Credentials'
 */
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


