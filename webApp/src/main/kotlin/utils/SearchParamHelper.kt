package utils

import kotlinx.browser.window
import org.w3c.dom.url.URLSearchParams

/**
 * Search key/value storage is Javascript specific.
 *
 * Used to manage navigation state.
 */
class SearchParamHelper {
   companion object {
      fun currentHref() = window.location.href
      fun currentPathname() = window.location.pathname
      fun currentSearch() = window.location.search
      fun currentSearchParams() = URLSearchParams(window.location.search)
      fun replaceSearchParams(params: URLSearchParams) =
         window.history.replaceState(null, "", "?$params")
      fun pushSearchParams(params: URLSearchParams) =
         window.history.pushState(null, "", "?$params")

      /*
       * NOTE: Avoid use of 'global' search params beyond these exceptions.
       */

      fun isDebug(key:String) = currentSearchParams().get("debug")?.equals(key) == true

      fun isDebug() = !currentSearchParams().get("debug").isNullOrBlank()
      fun isVerbose() = currentSearchParams().get("debug").equals("v")

      // Useful to test our client against prod data.
      fun isLocalHost() = window.location.hostname == "localhost"
      fun isEmulator() = isLocalHost() && !bypassEmu()
      fun bypassEmu() = !currentSearchParams().get("bypassEmu").isNullOrBlank()
   }
}
