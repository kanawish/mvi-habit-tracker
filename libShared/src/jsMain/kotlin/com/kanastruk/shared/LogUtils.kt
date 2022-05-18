package com.kanastruk.shared

actual fun logE(msg: String) {
    console.error(msg)
}

actual fun logW(msg: String) {
    console.warn(msg)
}

actual fun logD(msg: String) {
    console.log(msg)
}
