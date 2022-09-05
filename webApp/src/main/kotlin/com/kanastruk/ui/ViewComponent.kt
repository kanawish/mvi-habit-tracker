package com.kanastruk.ui

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.w3c.dom.HTMLInputElement
import utils.mapDistinct
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KMutableProperty0

/**
 * Learnings about Coroutines / Flow in JS
 *
 * https://github.com/Kotlin/kotlinx.coroutines/tree/master/js
 * https://discuss.kotlinlang.org/t/recommendations-for-using-coroutines-in-kotlin-js/7011
 * https://itnext.io/taming-react-with-kotlin-js-and-coroutines-ef0d3f72b3ea
 *
 * TODO: Evaluate how to better handle "view trees".
 *   Having view components delegate "CoroutineScope by X" might be a pretty good way.
 *   Benefit would be to only have 'viewGroup' with a detach() function... (?)
 */
abstract class ViewComponent<M,E> : CoroutineScope {
   private var job = Job()
   override val coroutineContext: CoroutineContext
      get() = job

   open fun postInit() {}

   abstract fun attach(model: StateFlow<M>, handler: (E)->Unit = {})

   open fun detach() {
      job.cancel()
      job = Job()
   }

   /**
    * This utility function takes mapped values from the source Flow<T?>,
    * and will map them to String, replacing possible null-mappings with "".
    *
    * Then, it will launch a collect on the resulting Flow<String>, assigning
    * the string values on the provided mutable string property.
    */
   fun <T> Flow<T?>.assignDistinct(m:(T)->String?, p: KMutableProperty0<String>): Job {
      return mapDistinct { nullable -> nullable?.let { m(it) } ?: "" }
         .launchCollect { p.set(it) }
   }

   fun <T> Flow<T?>.assignDistinct(m:(T)->String?, input: HTMLInputElement): Job =
      assignDistinct(m,input::value)

   fun <T> Flow<T?>.assignDistinct(m:(T)->Number?, p: KMutableProperty0<String>): Job {
      return mapDistinct { nullable -> nullable?.let { m(it) }?.toString() ?: "" }
         .launchCollect { p.set(it) }
   }

   fun <T> Flow<T?>.assignDistinct(m:(T)->Number?, input: HTMLInputElement): Job =
      assignDistinct(m,input::value)

   /**
    * DSL shorthand for strings.launchCollect { htmlElement.value = it }
    */
   fun Flow<String>.assign(p: KMutableProperty0<String>): Job {
      return launchCollect { p.set(it) }
   }

   /**
    * Shorthand function for `launch { flow.collect(collector) }`
    *
    * Consider using ViewComponent's assignDistinct() instead.
    *
    * NOTE: This is likely not a pattern that makes sense outside 'javascript land'.
    * TODO: Do a more thorough investigation of coroutine/Flow usage with JS vs JVM
    *
    * @see assignDistinct
    */
   fun <T> Flow<T>.launchCollect(collector: FlowCollector<T>): Job {
      return launch {
         this@launchCollect.collect(collector)
      }
   }

}