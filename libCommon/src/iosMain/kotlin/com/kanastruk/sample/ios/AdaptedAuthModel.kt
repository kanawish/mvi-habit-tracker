package com.kanastruk.sample.ios

import com.kanastruk.sample.common.model.AuthModel
import com.kanastruk.sample.common.model.AuthState

@Suppress("Unused", "MemberVisibilityCanBePrivate") // Members are called from Swift
class AdaptedAuthModel(val authModel: AuthModel) {
    val store: FlowAdapter<AuthState> = FlowAdapter(authModel,authModel.authStore)
}
