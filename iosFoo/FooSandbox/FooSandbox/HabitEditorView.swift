//
//  HabitEditorView.swift
//  FooSandbox
//
//  Created by Etienne Caron on 2022-07-21.
//

import SwiftUI

struct EditRow :Identifiable{
    let label: String
    let systemImage: String
    var id: String {label}

    init(_ label: String, _ systemImage: String) {
        self.label = label
        self.systemImage = systemImage
    }

}

struct FakeHabit {
    @State var title = ""
    @State var frequency = ""
    @State var type = ""
    @State var goal = ""
}

struct HabitEditorView: View {
    private let x = [
        EditRow("Name", "person"),
        EditRow("Frequency", "calendar"),
        EditRow("Type", "gearshape.2"),
        EditRow("Goal", "target"),
    ]
    private let fields = ["Name", "Frequency", "Type", "Goal"]

    @State private var username: String = ""
    @State private var password: String = ""

    var body: some View {
        List {
            ForEach(x) { row in
                EditorField(row.systemImage, row.label, $username)
            }
        }
    }
}

struct EditorField: View {
    private let systemImage: String
    private let label: String
    private let binding: Binding<String>

    init(_ si: String, _ l: String, _ b: Binding<String>) {
        systemImage = si
        label = l
        binding = b
    }

    var body: some View {
        HStack {
            Image(systemName: systemImage)
                .resizable()
                .scaledToFit()
                .frame(width:18)
                .foregroundColor(Color.gray)
            Spacer()
            TextField(label, text: binding)
        }
    }
}

struct Foo: View {
    @State private var username: String = ""
    @State private var password: String = ""

    var body: some View {
        List {
            Section(content: {
                TextField("Username", text: $username)
            }, header: {
                Label("User", systemImage: "person")
            })
            Section(content: {
                TextField("Username", text: $username)
            }, header: {
                Label("User", systemImage: "person")
            })
            Section {
                HStack {
                    Label("User", systemImage: "person").padding(.trailing, 4.0)
                    Spacer()
                    TextField("Username", text: $username)
                }
                HStack {
                    Image(systemName: "person")
                    Spacer()
                    TextField("Username", text: $username)
                }
            }
        }
    }
}

struct HabitEditorView_Previews: PreviewProvider {
    static var previews: some View {
        HabitEditorView()
        //.previewLayout(.sizeThatFits)
    }
}
