//
// Created by Etienne Caron on 2022-07-28.
//

import Foundation
import libCommon

func startKoin() {
    let userDefaults = UserDefaults()
    let iosAppInfo = IosAppInfo()

    let koinApp = KoinIOSKt.doInitKoinIos(
        userDefaults: userDefaults,
        appInfo: iosAppInfo
    )
    _koin = koinApp.koin
}

private var _koin: Koin_coreKoin?
var koin: Koin_coreKoin {
    return _koin!
}

class IosAppInfo: AppInfo {
    let appId: String = Bundle.main.bundleIdentifier!
}

