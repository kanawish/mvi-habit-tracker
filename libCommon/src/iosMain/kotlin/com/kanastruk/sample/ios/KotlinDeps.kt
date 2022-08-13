package com.kanastruk.sample.ios

import com.kanastruk.sample.common.model.AuthModel
import com.kanastruk.sample.common.model.HabitModel
import org.koin.core.component.KoinComponent

@Suppress("unused")
object KotlinDeps: KoinComponent {
    fun getAuthModel() = getKoin().get<AuthModel>()
    fun getHabitModel() = getKoin().get<HabitModel>()
}