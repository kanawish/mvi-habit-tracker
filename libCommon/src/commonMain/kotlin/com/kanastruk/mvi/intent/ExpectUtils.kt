package com.kanastruk.mvi.intent

/**
 * E:Expected child, SC: Sealed class parent.
 * When building a state machine using the sealed class approach, 'expecting()'
 * allows you to create Intents that will log a warning and no-op when
 * trying to execute an intent off of the wrong 'parent' (or 'old') state.
 */
inline fun <reified E : SC,SC> expectingIntent(crossinline block: (E) -> SC): Intent<SC> {
   return Intent { old ->
      when (old) {
         is E -> block(old)
         else -> {
            old
         }
      }
   }
}
