# MVI Multiplatform Talk

The story is "sharing KMPP learnings from my day to day". 

## Learnings 

Big pieces, drilling down into details:

NOTE: Take some of the 'leaves' of learning, and bring them back as narrative points as I tell the story.

Story structure: 
1. Multiplat Kotlin promising, it's all about code sharing, without losing out on native features. [s01-04]
2. But how much can we really achieve the code-sharing promise? MVI works well for that. [s05-0? explain MVI]
3. My experience is that [M]odels are greatly re-usable. As long as you find ways to stay nimble. Over-prescriptive frameworks are total tar-pits. Common libs are where the payoffs live. Jetbrain's own language-level features are top-tier. Then, you can dig into 3rd party libs. There's the 'vital basics' (Koin,Okio), and the more complex 'framework-ish' choices (Compose, SwiftUI & Combine, Firebase) that don't always cover all your platforms.

- [x] Arguably, strength (native look&feel snobs rejoice) is also a weakness (code reuse)
	- [x] Why did _we_ pick KMPP? 'Punch above my weight'.
- [x] MVI works really well in promoting code reuse.
	- [x] What _is_ MVI anyway?
	- *In what ways do we diverge from other MVI approaches?*
		- Aim for simplicity ['as complex as it needs']
			- 'stateless' functional over layered instances e.g. MVVM VM -> 'formatting'.
			- avoiding 'mvi framework libraries' in favor of 'simple components' concept.
	- ... [Add the MVI spiel from lessons] 
	- [move lower] process + 'intent dispatcher', vs `Model.processXYZ()`
- MVI - how-to common stuff
	-  
- KMPP 
	- Still beta
	- Common libs are great, some 'foundationals':
		- Jetbrains
			- Ktor, Coroutines, Flows
			- Serialization, kotlinx-datetime & Stdlib
		- Others: Koin, Okio, Kermit
	- "Platform" considerations: non-common libs that play nice:
		- Firebase 
		- Compose
		- Swift-UI/reactive stack
- ...		
- ...
- Other big libs: sqldelight, apollo-kotlin, kodein 
- 'Thank you's and 'look for's: Touchlabs, iOS reactive articles, 

[pile]
		- "T" shaped: *adj.* Having skills and knowledge that are both deep and broad.
			- Horizontal: basic Kotlin programming knowledge applies everywhere
				- Client-side logic
					- Android & iOS
					- Desktop
				- Server-side logic
			- Vertical: requires specialized knowledge
				- Android: Compose & Platform [Kotlin]
				- iOS: SwiftUI & Platform [Swift]
				- Web: React & npm ecosystem [Kotlin/JS interop]
				- Server-side: ecosystems and scaling know-how. [Kotlin JVM]
		- We can't specialize in everything. 
			- Leveling up "Kotlin know-how" is useful across most of the T
			- Common language with diverse viewpoints: win win? 
				- Common language builds cohesion.
				- Diverse viewpoints help with critical thinking.
- How to 'enable' mpp benefit? [narrative thread]
	- Re-using and sharing code [MVI]
	- Good 'common' libraries!
	- clean and easy interops (Arguably where reality can fall short of the dream.)


(Italics indicate 'ongoing story point')

## Research 

- [MPP libs directory](https://libs.kmp.icerock.dev/)
	- unrelated: data2viz would be a nice 'to explore'.
- ...

## iOS Notes

22-07-26

Key items re: subscribing to Flows

- https://dev.to/touchlab/working-with-kotlin-coroutines-and-rxswift-24fa
- https://johnoreilly.dev/posts/kotlinmultiplatform-swift-combine_publisher-flow/


22-07-24

First run of talk online is coming up Tuesday. Bit last second as usual. Today, I'd really like to be 'done' or close to that with the code.

- [ ] Working on getting the Ktor calls working. 
	- They're a key part of making 'common' Models.
- [ ] Valid screens in iOS & Web ['read-only' List of Habits?]
- [ ] Connect Android to common Model?

Slides
- [ ] Get structure going, copy from course deck where appropriate.
- [ ] Re-style to match 'target' look & feel? Maybe with Roberta helping.

---

Week of 22-07-18 

I'm trying to put together an iOS app for my Multiplatform Talk soon coming up.

I'm working off the KaMPKit project, and I realized it's worth documenting the 'start the iOS project' part.

Right now, looking at it, it looks simple (?)

- Use cocoapods to start an iOS project, as a sub-directory of the multiplat project.
- Once that's done, cocoapods generates all the xcode build files for us.
- Manual step: add a 'build mpp lib' step to the iOS build, so it works 'independently' from gradle project. 
- (not really independent since xcode build step uses gradle to create that lib)

[Getting started with CocoaPods](https://guides.cocoapods.org/using/using-cocoapods.html) 

Now, the current habit tracker uses Retrofit and OkHttp. That's not (yet) 
available outside JVM-land, so I believe we'll need to implement something under 'common' with Ktor.

Once we use Firebase APIs, things get much easier. But not there yet in the story...

The Pod setup in the ios project expects a `shared.podspec` file in the shared lib module. The module uses the Jetbrains [native-cocoapods](https://kotlinlang.org/docs/native-cocoapods.html) gradle plugin.

My `libCommon/libCommon.podspec` file was generated off of my cocoapods gradle plugin 'sync' process. 

Which in turn made the `pod install` command possible from the iOS project root.

Ran into a datetime [issue](https://youtrack.jetbrains.com/issue/KT-52554), fixed with version uptick.

Note that there used to be a manual step to run custom build script, that's now part of the cocoapods setup from what I can see.


---

IMPORTANT LESSONS? 

- consuming Flows from Swift/UI
- sealed class conversion(?)
- hooking up SwiftUI to our 'M'
- notes on reusable designs(?)


### Swift study notes and links

- Swift UI [Cookbook](https://learning.oreilly.com/videos/swiftui-the/9781801070676/9781801070676-video1_1/)
- ...
- [Environment](https://developer.apple.com/documentation/swiftui/environment)
- [property wrappers](https://www.swiftbysundell.com/articles/property-wrappers-in-swift/)
- [UI design guidelines](https://developer.apple.com/design/human-interface-guidelines/components/all-components)
- [design guidelines](https://www.learnui.design/blog/ios-design-guidelines-templates.html#ui-elements--controls)
- 