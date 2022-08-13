package utils

import kotlin.js.Console

fun debug(key:String, block: Console.()->Unit) {
    if(SearchParamHelper.isDebug(key)) console.block()
}

fun debug(block: Console.()->Unit) {
    if(SearchParamHelper.isDebug()) console.block()
}

fun verbose(block: Console.()->Unit) {
    if(SearchParamHelper.isVerbose()) console.block()
}

// See https://stackoverflow.com/questions/105034/how-to-create-a-guid-uuid
fun uuidv4():String {
    val jsUUID = js("'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {\n" +
            "    var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);\n" +
            "    return v.toString(16);\n" +
            "  })")
    return jsUUID
}