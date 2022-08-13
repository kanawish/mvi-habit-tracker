package com.kanastruk.sample.common.rest.habit

import kotlinx.serialization.Serializable

/**
 * Universal 'new key' response when doing a POST on our backend.
 */
@Serializable
data class NameResponse(val name:String)
