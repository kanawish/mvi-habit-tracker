package com.kanastruk.ui

import com.kanastruk.sample.common.model.HabitState
import com.kanastruk.sample.habit.ui.HabitsViewEvent
import kotlinx.browser.document
import kotlinx.coroutines.flow.StateFlow
import kotlinx.html.div
import kotlinx.html.dom.create
import kotlinx.html.h1

class HabitsListView():ViewComponent<HabitState, HabitsViewEvent>() {
    val titleHeader = document.create.div("container") {
        h1("pt-3") { +"Habits Tracker" }
    }
    val footer = document.createFooter("mt-auto text-center", "© 2021-2022 Kanastruk service conseil inc.")

    override fun attach(model: StateFlow<HabitState>, handler: (HabitsViewEvent) -> Unit) {
        TODO("Not yet implemented")
    }
}