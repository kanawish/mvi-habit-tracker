plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

group = "com.kanastruk.sample.common"
version = "1.0-SNAPSHOT"

android {
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
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
                api(project(":libShared"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0")
                implementation("io.insert-koin:koin-core:3.2.0")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("io.insert-koin:koin-test:3.2.0")
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