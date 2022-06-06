package com.kanastruk.mvi.intent

/**
 * Intents for now are designed as 'non suspendable'. In-flight async processes
 * should be 'mapped' with <S> states.
 *
 * I.e. 'Closed' -> 'Opening' -> ..., or 'State.isLoading', etc.
 */
fun interface Intent<S> {
   fun reduce(old: S): S
}
