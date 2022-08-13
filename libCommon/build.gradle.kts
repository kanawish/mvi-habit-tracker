plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization") version "1.7.10"
    kotlin("native.cocoapods")
    id("com.android.library")
}

android {
    namespace = "com.kanastruk.sample.common"
    compileSdk = 32
    defaultConfig {
        minSdk = 24
        targetSdk = 32
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

version = "1.0-SNAPSHOT"
dependencies {
    androidTestImplementation("org.testng:testng:6.9.6")
}

android {
    configurations {
        create("androidTestApi")
        create("androidTestDebugApi")
        create("androidTestReleaseApi")
        create("testApi")
        create("testDebugApi")
        create("testReleaseApi")
    }
}

kotlin {
    jvm("vanillaJvm") {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
    }
    android()
    ios()
    iosSimulatorArm64()
    js(IR) {
        browser {
            commonWebpackConfig {
                cssSupport.enabled = true
            }
        }
        binaries.executable()
    }
    sourceSets {
        all {
            languageSettings.apply {
                optIn("kotlin.RequiresOptIn")
                optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
            }
        }
    }

    // NOTE: We're using 1.7.0, so we shouldn't need this block.
    // Enable concurrent sweep phase in new native memory manager. (This will be enabled by default in 1.7.0)
    // https://kotlinlang.org/docs/whatsnew1620.html#concurrent-implementation-for-the-sweep-phase-in-new-memory-manager
/*
    targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget> {
        binaries.all {
            freeCompilerArgs += "-Xgc=cms"
        }
    }
*/

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-stdlib:1.7.10")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")

                api("org.jetbrains.kotlinx:kotlinx-datetime:0.3.3")

                implementation("com.squareup.okio:okio:3.2.0")

                implementation("io.insert-koin:koin-core:3.2.0")

                api("co.touchlab:kermit:1.0.3")
                implementation("com.russhwolf:multiplatform-settings:0.9")

                implementation("io.ktor:ktor-serialization-kotlinx-json:2.0.3")
                implementation("io.ktor:ktor-client-core:2.0.3")
                implementation("io.ktor:ktor-client-serialization:2.0.3")
                implementation("io.ktor:ktor-client-logging:2.0.3")
                implementation("io.ktor:ktor-client-content-negotiation:2.0.3")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")
                implementation("io.insert-koin:koin-test:3.2.0")
            }
        }
        val vanillaJvmMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-cio:2.0.3")
            }
        }
        val vanillaJvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                api("io.ktor:ktor-client-cio:2.0.3")
            }
        }
        val androidTest by getting { dependencies {} }
        val iosMain by getting {
            dependencies {
                // No https for CIO? uh.. https://github.com/joreilly/PeopleInSpace/issues/69
                implementation("io.ktor:ktor-client-ios:2.0.3")
            }
        }
        val iosTest by getting {
            dependencies {

            }
        }
        val iosSimulatorArm64Main by getting {
            dependsOn(iosMain)
        }
        val iosSimulatorArm64Test by getting {
            dependsOn(iosTest)
        }
        val jsMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-stdlib-js:1.7.10")
                implementation("org.jetbrains.kotlinx:kotlinx-html-js:0.8.0")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:1.6.4")

                implementation("io.ktor:ktor-client-js:2.0.3")
            }
        }
        val jsTest by getting {}
    }

    cocoapods {
        summary = "Common library for my MVI Habit Tracker sample"
        homepage = "https://github.com/kanawish/mvi-habit-tracker"
        framework {
            isStatic = false // SwiftUI preview requires dynamic framework
        }
        ios.deploymentTarget = "15.4"
        podfile = project.file("../ios/Podfile")
    }
}
