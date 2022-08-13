//
//  ContentView.swift
//  FooSandbox
//
//  Created by Etienne Caron on 2022-07-20.
//

import SwiftUI

/*
struct ContentView: View {
    var body: some View {
        NavigationView {
            VStack {
                NavigationLink(destination: , label: <#T##@ViewBuilder () -> Label##@ViewBuilder () -> Label#>) {}
            }
        }

    }
}
*/

struct ContentView:View {
    @State private var animals = ["Cats", "Lizards", "Ants", "Tubers", "DIYers"]

    var body: some View {
        NavigationView {
            List {
                ForEach(animals, id: \.self) { animal in
                    Text("🐾 \(animal)")
                }.onDelete(perform: removeAnimal)
            }
                .navigationBarItems(trailing: EditButton())
                .navigationBarTitle(Text("EditButtonView"), displayMode: .inline)
        }
    }

    func removeAnimal(at offsets: IndexSet) {
        animals.remove(atOffsets: offsets)
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
