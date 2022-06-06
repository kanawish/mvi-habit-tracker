package com.kanastruk.fb.rest.auth

/**
 * Response we get from SecureTokenApi when refreshing,
 * with utility function to convert into 'Credentials'
 */
data class RefreshResponse(
    val access_token: String,
    val expires_in: String,
    val id_token: String,
    val project_id: String,
    val refresh_token: String,
    val token_type: String,
    val user_id: String
) {
    fun toCredentials() = Credentials(id_token, user_id, refresh_token)
}
