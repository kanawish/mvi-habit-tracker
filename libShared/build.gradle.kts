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

        val androidMain by getting {}
        val androidTest by getting {}

        val iosMain by getting {}
        val iosTest by getting {}

        val vanillaJvmMain by getting {}
        val vanillaJvmTest by getting {}

        val jsMain by getting {}
        val jsTest by getting {}
    }
}