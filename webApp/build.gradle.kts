plugins {
    kotlin("js")
    id("org.jetbrains.kotlin.plugin.serialization") version "1.7.10"

}

// 22/08/01 Current AS problem: https://youtrack.jetbrains.com/issue/KTIJ-22303

// NOTE: See https://kotlinlang.org/docs/reference/js-project-setup.html
//   Also found working examples here https://github.com/rjaros/kvision/blob/master/build.gradle.kts
kotlin {
    // NOTE: previously, js(IR) didn't support source-maps. Track progress below:
    //   https://youtrack.jetbrains.com/issue/KT-39447?_gl=1*133g0g5*_ga*NDkwNzI2NDM3LjE2MDMzNzE1MzE.*_ga_J6T75801PF*MTYyODU2NjI1NC4zLjEuMTYyODU2Nzc5MS4w&_ga=2.9886837.1974814920.1628566173-490726437.1603371531
    js(IR) {
        useCommonJs() // NOTE: 'Fix' vs UMD https://github.com/Kotlin/dukat/issues/106
        binaries.executable()
        browser {
            commonWebpackConfig {
                cssSupport.enabled = true
            }
            testTask {
                testLogging.showStandardStreams = true
            }
        }
    }
}

// NOTE: https://youtrack.jetbrains.com/issue/KT-52776/KJS-Gradle-Webpack-version-update-despite-yarnlock-breaks-KotlinJS-build
// NOTE: https://github.com/studoverse/campus-qr/commit/2d66d11d84b038f319e78f40c2cbbb7d5ced45a3
rootProject.extensions.configure<org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension> {
    versions.webpackCli.version = "4.10.0"
}

dependencies {
    implementation(project(":libCommon"))

    implementation("org.jetbrains.kotlinx:kotlinx-html-js:0.8.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.jetbrains.kotlin-wrappers:kotlin-extensions:1.0.1-pre.347")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")

/*
    implementation(npm("bootstrap", "5.1.3"))
    implementation(npm("bootstrap-icons", "1.7.0"))
    implementation(npm("bootstrap-table", "1.18.3")) // add-ons not easy to enable.
*/

    api("io.kvision:jquery-kotlin:1.0.0")
    implementation(npm("jquery", "^3.5.1"))

}
