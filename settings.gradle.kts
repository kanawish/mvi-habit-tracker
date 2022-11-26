
pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

rootProject.name = "MVIHabitTracker"

include(":libShared")
include(":libCommon")
include(":libAndroid")
include(":app")
