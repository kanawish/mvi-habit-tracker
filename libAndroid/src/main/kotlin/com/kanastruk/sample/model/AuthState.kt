package com.kanastruk.sample.model

import com.kanastruk.fb.rest.auth.Credentials

/**
 * AuthState uses the sealed class 'state machine' approach,
 * which is key in managing state transitions safely.
 *
 * @startuml
 * INITIALIZING:
 * [*] --> INITIALIZING
 * INITIALIZING --> READY
 * INITIALIZING --> ERROR
 * READY --> INITIALIZING : resetCredentials()
 * REFRESHING --> READY
 * READY --> REFRESHING : refreshCredentials()
 * REFRESHING --> ERROR
 * ERROR --> INITIALIZING : resetCredentials()
 * @enduml
 *
 * Simplistic auth for sample app
 *
 * - starting: load existing creds, create new creds if none.
 * - ready: existing creds, user initiated reset()/refresh()
 * - error: can only really try reset()?
 */
sealed class AuthState {
    object Init : AuthState()
    data class Ready(val credentials: Credentials) : AuthState()
    data class Refreshing(val credentials: Credentials) : AuthState()
    data class Error(val msg: String) : AuthState()
}
