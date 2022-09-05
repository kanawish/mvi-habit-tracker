package com.kanastruk.sample.habit.ui

sealed class AuthViewEvent {
    object RefreshCredentialsClick : AuthViewEvent()
    object AttemptReloadClick : AuthViewEvent()
}