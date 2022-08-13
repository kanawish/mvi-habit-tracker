package com.kanastruk.sample.common.rest

import kotlinx.serialization.Serializable

/**
 * Universal error response when a Rest API call goes wrong.
 */
@Serializable
data class ErrorResponse(
    val error: String
)
