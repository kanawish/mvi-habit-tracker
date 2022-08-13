package com.kanastruk.sample.common.rest.auth

import kotlinx.serialization.Serializable

/**
 * The IdentityToolkitApi requires this parameter.
 */
@Serializable
data class IdentityToolkitParam(val returnSecureToken: Boolean = true)
