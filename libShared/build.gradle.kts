plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

group = "com.kanastruk.sample.shared"
version = "1.0-SNAPSHOT"

android {
    // sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    compileSdk = 32
    defaultConfig {
        minSdk = 24
        targetSdk = 32
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
    }
}

kotlin {
    jvm("vanillaJvm") {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
    }
    // NOTE: https://youtrack.jetbrains.com/issue/KT-49109
    // NOTE: https://stackoverflow.com/questions/60534770/exception-when-building-a-kotlin-js-project-error-package-json-name-contains-i
    js(IR) {
        browser {
            commonWebpackConfig {
                cssSupport.enabled = true
            }
        }
        binaries.executable()
    }
    ios()
    // NOTE: https://stackoverflow.com/questions/40743713/command-line-tool-error-xcrun-error-unable-to-find-utility-xcodebuild-n?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
    macosX64("macos")
    android()

    sourceSets {
        all {
            languageSettings.apply {
                optIn("kotlin.RequiresOptIn")
                optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
            }
        }
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0")
                implementation("io.insert-koin:koin-core:3.0.2")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("io.insert-koin:koin-test:3.0.2")
            }
        }

        val androidMain by getting {}
        val androidTest by getting {
            dependencies {
                implementation("junit:junit:4.13.2")
            }
        }
        val iosMain by getting {}
        val iosTest by getting {}

        val vanillaJvmMain by getting {}
        val vanillaJvmTest by getting {}

        val jsMain by getting {}
        val jsTest by getting {}
    }
}