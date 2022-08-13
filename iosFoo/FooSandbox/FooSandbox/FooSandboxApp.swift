//
//  FooSandboxApp.swift
//  FooSandbox
//
//  Created by Etienne Caron on 2022-07-20.
//

import SwiftUI

@main
struct FooSandboxApp: App {
    @Environment(\.scenePhase) var scenePhase

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
                .onChange(of: scenePhase, perform: { phase in
                    switch phase {
                    case .active:
                        print("now active")
                    case .inactive: print("now inactive")
                    case .background:
                        print("now in background")
                    @unknown default:
                        print("unknown")
                    }
                })
    }

}
