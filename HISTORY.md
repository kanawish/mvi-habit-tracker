# History

When working with the 'newest and greatest' Android and Kotlin projects, I find myself running into a large amount of build issues and intellisense annoyances.

Here is a centralized (and time-stamped) history of these issues, with solution or workaround, when found. 

- Firestore [backups](https://medium.com/google-cloud/firestore-backups-the-easy-way-with-cloud-workflows-3a96a434d3c7) will be needed.
- Some basics to [fetch list of peripherals](https://stackoverflow.com/questions/34592179/how-get-the-list-of-paired-bluetooth-devices-in-swift).
- [MoreSFSymbols](https://github.com/cameronshemilt/MoreSFSymbols)
- `@StateObject` advertised as the [solution](https://www.avanderlee.com/swiftui/stateobject-observedobject-differences/#:~:text=%40StateObject%20and%20%40ObservedObject%20have%20similar,you%20can%20use%20the%20%40ObservedObject.) to the lifecycle.
- About [cleanup / lifecycle](https://stackoverflow.com/questions/68633861/swiftui-view-init-called-multiple-times#:~:text=It%20is%20common%20for%20init,are%20only%20once%20at%20startup.), [possible solution](https://github.com/Bahn-X/swift-composable-navigator/issues/73)? hmm..
- [ ] Q. RW's tutos are great, but beg the question 'how about cleanup'? 
- SwiftUI FirebaseUI [example](https://github.com/firebase/FirebaseUI-iOS/issues/811)
- AppCode screen [flicker fix](https://youtrack.jetbrains.com/issue/JBR-4680/idea-window-flickers-while-changing-the-screen-brightness)
- Firebase config in init() [should be fine](https://stackoverflow.com/questions/62626652/where-to-configure-firebase-in-my-ios-app-in-the-new-swiftui-app-life-cycle-with)
- 🌟 Firebase [type mapping](https://peterfriese.dev/posts/firestore-codable-the-comprehensive-guide/#mapping-arrays) in Swift.
- 🌟 RW's [swift/firebase tuto](https://www.raywenderlich.com/11609977-getting-started-with-cloud-firestore-and-swiftui#toc-anchor-006) is clearer to me than HW.
- RW's [Swift ref vs. value type](https://www.raywenderlich.com/9481-reference-vs-value-types-in-swift) article 
- Hacking swift rarely groks for me: [ObservedObject](https://www.hackingwithswift.com/quick-start/swiftui/whats-the-difference-between-observedobject-state-and-environmentobject)
- [Cocoapods warning fix](https://stackoverflow.com/questions/40599454/use-the-inherited-flag-or-remove-the-build-settings-from-the-target-c/43799712#43799712)
- For later: [Transfering](https://stackoverflow.com/questions/24688716/transferring-an-app-to-another-firebase-account) Firebase ownership
- pod [install vs update](https://guides.cocoapods.org/using/pod-install-vs-update.html)
	- tldr; use install when adding new pods.
- Starting new iOS project with [CocoaPods](https://guides.cocoapods.org/using/using-cocoapods.html)
- Kotlin JS sync issues [YouTrack](https://youtrack.jetbrains.com/issue/KT-49109), [S/O](https://stackoverflow.com/questions/60534770/exception-when-building-a-kotlin-js-project-error-package-json-name-contains-i)
- ✅ [Unable to find utility `xcodebuild`](https://stackoverflow.com/questions/40743713/command-line-tool-error-xcrun-error-unable-to-find-utility-xcodebuild-n)
