package com.kanastruk.fb.rest

/**
 * Universal error response when a Rest API call goes wrong.
 */
data class ErrorResponse(
    val error: String
)