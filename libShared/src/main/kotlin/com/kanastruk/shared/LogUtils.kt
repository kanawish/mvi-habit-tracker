package com.kanastruk.shared

import android.util.Log

actual fun logE(msg: String) {
    Log.e("libShared", msg)
}

actual fun logW(msg: String) {
    Log.w("libShared", msg)
}

actual fun logD(msg: String) {
    Log.d("libShared", msg)
}
