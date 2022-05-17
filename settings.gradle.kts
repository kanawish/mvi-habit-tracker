
pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

rootProject.name = "MVI Habit Tracker"
include(":app")
include(":libAndroid")
include(":libCommon")
include(":libShared")
