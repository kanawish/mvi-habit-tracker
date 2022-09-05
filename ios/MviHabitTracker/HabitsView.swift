//
//  ContentView.swift
//  MviHabitTracker
//
//  Created by Etienne Caron on 2022-07-20.
//

import SwiftUI
import libCommon

struct HabitRowView:View {
    var habit: Habit
    var body: some View {
        VStack(alignment: .leading, spacing: 3) {
            Text(habit.name)
                .foregroundColor(.primary)
                .font(.headline)
            HStack(spacing: 3) {
                Label("\(habit.goal) / \(habit.frequency) / \(habit.unit)", systemImage: "target")
            }
                .foregroundColor(.secondary)
                .font(.subheadline)
        }
    }
}

/*
 Some notes
 - iterate lists https://www.hackingwithswift.com/quick-start/swiftui/how-to-create-views-in-a-loop-using-foreach
 */
struct HabitsView: View {
    @StateObject var obsAuthModel = ObsAuthModel()
    @StateObject var obsHabitModel = ObsHabitModel()

    var body: some View {
        let mappedHabits: [Habit] =
            obsHabitModel.habitState?.habits.values.map { $0 }
                ?? [Habit]()

        VStack {
            // ?? TODO: if let cacheState = obsHabitModel.habitState?.cacheState as Failure
            List {
                ForEach(mappedHabits, id: \.self) { element in
                    HabitRowView(habit: element)
                }
            }.frame(maxWidth: .infinity)
            HStack {
                let refreshAction = {obsAuthModel.processRefresh()}
                Button(action: refreshAction) {
                    Label("Auth Refresh", systemImage: "key")
                }.buttonStyle(.bordered)
                    .frame(maxWidth: .infinity)
                let reloadAction = {
                    obsHabitModel.processReload()
                }
                Button(action: reloadAction) {
                    Label("Reload", systemImage: "arrow.clockwise")
                }.buttonStyle(.bordered)
                    .frame(maxWidth: .infinity)
            }.padding(.bottom, 10)
        }.onAppear {
            obsAuthModel.activate()
            obsHabitModel.activate()
        }.onDisappear {
            obsAuthModel.deactivate()
            obsHabitModel.deactivate()
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        HabitsView()
    }
}
