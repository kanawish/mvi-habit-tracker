//
// Created by Etienne Caron on 2022-07-28.
//

import Combine
import libCommon

class ObsHabitModel:ObservableObject {
    private let adapted:AdaptedHabitModel
    private var cancellables = [AnyCancellable]()

    @Published var habitState:HabitState?

    init() {
        adapted = AdaptedHabitModel(habitModel: KotlinDeps.shared.getHabitModel())
    }

    func activate() {
        doPublish(adapted.store) { [weak self] (state:HabitState) in
            self?.habitState = state
        }.store(in: &cancellables)
    }

    func processReload() {
        adapted.habitModel.processReloadIntent()
    }

    func deactivate() {
        cancellables.forEach { $0.cancel() }
        cancellables.removeAll()
    }
}

class ObsAuthModel:ObservableObject {
    private let adapted:AdaptedAuthModel
    private var cancellables = [AnyCancellable]()

    @Published var authState:AuthState?

    init() {
        adapted = AdaptedAuthModel(authModel: KotlinDeps.shared.getAuthModel())
    }

    func activate() {
        doPublish(adapted.store) { [weak self] (state:AuthState) in
            self?.authState = state
        }.store(in: &cancellables)
    }

    func processRefresh() {
        adapted.authModel.processRefreshToken()
    }

    func deactivate() {
        cancellables.forEach { $0.cancel() }
        cancellables.removeAll()
    }
}

class ObservableMockModel:ObservableObject {
    private let adapted = AdaptedMockModel(model: MockModel())

    @Published
    var counter:Int = -1

    private var cancellables = [AnyCancellable]()

    func reset() {
        adapted.model.processReset()
    }

    // Called from SwiftUI view.
    func activate() {
        print("activate()")
        doPublish(adapted.store) { [weak self] (kInt: KotlinInt) in
            self?.counter = Int(kInt)
            print("[\(self?.counter)] publish(\(kInt))?")
        }.store(in: &cancellables)
    }

    // Called from SwiftUI view.
    func deactivate() {
        print("deactivate(cCount=\(cancellables.count))")
        cancellables.forEach { $0.cancel() }
        cancellables.removeAll()
    }
}
