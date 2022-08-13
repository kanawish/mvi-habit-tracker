package com.kanastruk.sample.common.rest.auth

import kotlinx.serialization.Serializable

/**
 * Response we get from SecureTokenApi when refreshing,
 * with utility function to convert into 'Credentials'
 */
@Serializable
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
