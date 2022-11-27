package com.kanastruk.sample.habit.ui

import androidx.compose.runtime.Composable
import com.kanastruk.sample.model.AuthState
import com.kanastruk.sample.model.CacheState
import com.kanastruk.sample.model.HabitState

/**
 * This class is an example how you can handle various 'auth states' in a consistent way
 * across your application.
 *
 * @param wrappedContent content shown when CacheState is LOADED or MODIFIED
 */
@Composable
fun AuthWrapper(
    authState: AuthState,
    habitState: HabitState,
    handler:(AuthViewEvent)->Unit,
    wrappedContent: @Composable () -> Unit,

    ) {
    when (authState) {
        AuthState.Init -> BusyStateView(label = "AUTH INITIALIZING")
        is AuthState.Refreshing,
        is AuthState.Ready -> when (habitState.cacheState) {
            CacheState.BUSY,
            CacheState.LOADING -> BusyStateView(habitState.cacheState.name)
            CacheState.FAILED -> ProposeRefresh(
                habitState.cacheState.name,
                authState is AuthState.Refreshing,
                handler
            )
            CacheState.LOADED,
            CacheState.MODIFIED -> wrappedContent()
        }
        is AuthState.Error -> BusyStateView(label = "AUTH ERROR ${authState.msg}")
    }
}
