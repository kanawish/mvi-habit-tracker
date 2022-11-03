package com.kanastruk.shared

import timber.log.Timber

actual fun logE(msg: String) {
    Timber.e(msg)
}

actual fun logW(msg: String) {
    Timber.w(msg)
}

actual fun logD(msg: String) {
    Timber.d(msg)
}
