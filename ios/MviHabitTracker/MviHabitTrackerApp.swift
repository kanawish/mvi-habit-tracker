//
//  MviHabitTrackerApp.swift
//  MviHabitTracker
//
//  Created by Etienne Caron on 2022-07-20.
//

import SwiftUI
import libCommon

@main
struct MviHabitTrackerApp: App {
    init() {
        startKoin()
    }

    var body: some Scene {
        WindowGroup {
            TopTabView()
        }
    }
}
