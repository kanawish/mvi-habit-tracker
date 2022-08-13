package com.kanastruk.sample.ios

import com.kanastruk.sample.common.model.HabitError
import com.kanastruk.sample.common.model.HabitModel
import com.kanastruk.sample.common.model.HabitState

@Suppress("Unused", "MemberVisibilityCanBePrivate") // Members are called from Swift
class AdaptedHabitModel(val habitModel: HabitModel) {
    val store: FlowAdapter<HabitState> = FlowAdapter(scope = habitModel, flow = habitModel.habitsStore)
    val errors: FlowAdapter<HabitError> = FlowAdapter(scope = habitModel, flow = habitModel.errors)
}