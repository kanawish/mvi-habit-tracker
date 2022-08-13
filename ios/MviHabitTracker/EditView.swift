//
// Created by Etienne Caron on 2022-07-29.
//

import SwiftUI
import libCommon

struct EditView: View {
    var body: some View {
        VStack {
            Text("TODO: Edit View")
            HStack {
                Button("Cancel") {
                    print("Cancel edit clicked")
                }.buttonStyle(.bordered)
                Button("Save") {
                    print("Save edit clicked")
                }.buttonStyle(.borderedProminent)
            }
        }
    }
}
