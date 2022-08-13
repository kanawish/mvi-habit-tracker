package com.kanastruk.sample.common.model

import com.kanastruk.sample.common.rest.auth.Credentials

/**
 * AuthState uses the sealed class 'state machine' approach,
 * which is key in managing state transitions safely.
 * @startuml
 * listfonts
 * @enduml
 *
 * @startuml
 * !theme k2 from /Users/kanawish/workspace/puml
 * skinparam BackgroundColor white
 * skinparam BackgroundColor transparent
 * skinparam StateBackgroundColor #EFEFEF
 * skinparam NoteBackgroundColor #FFFFEF
 * skinparam ArrowColor #000
 * skinparam ArrowFontColor #000
 * skinparam ArrowColor #FFF
 * skinparam ArrowFontColor #FFF
 *
 * [*] -> INITIALIZING : init()
 * INITIALIZING --> READY : success
 * INITIALIZING --> ERROR : failure
 * READY --> INITIALIZING : resetCredentials()
 * REFRESHING --> READY : success
 * READY --> REFRESHING : refreshCredentials()
 * REFRESHING --> ERROR : failure
 * ERROR --> INITIALIZING : resetCredentials()
 * @enduml
 *
 * Simplistic auth for sample app
 *
 * - starting: load existing creds, create new creds if none.
 * - ready: existing creds, user initiated reset()/refresh()
 * - error: can only really try reset()?uu
 */
sealed class AuthState {
    object Init : AuthState()
    data class Ready(val credentials: Credentials) : AuthState()
    data class Refreshing(val credentials: Credentials) : AuthState()
    data class Error(val msg: String) : AuthState()
}