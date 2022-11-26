plugins {
    id("com.android.library")
    kotlin("multiplatform")
}

version = "1.0-SNAPSHOT"

android {
    namespace = "com.kanastruk.sample.shared"
    compileSdk = 33
    defaultConfig {
        minSdk = 24
        targetSdk = 33
    }
}

kotlin {
    jvm("vanillaJvm") {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
    }
    js(IR) {
        browser {
            commonWebpackConfig {
                cssSupport.enabled = true
            }
        }
        binaries.executable()
    }
    ios()
    android()

    sourceSets {
        all {
            languageSettings.apply {
                optIn("kotlin.RequiresOptIn")
                optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
            }
        }
        val commonMain by getting {}
        val commonTest by getting {}

        val androidMain by getting {
            dependencies {
                // Logging
                implementation("com.jakewharton.timber:timber:4.7.1") // https://github.com/JakeWharton/timber/releases
            }
        }
        val androidTest by getting {
            dependencies {
                // Unit tests
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