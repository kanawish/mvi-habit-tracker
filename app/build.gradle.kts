plugins {
    id("com.android.application")
    kotlin("android")
}

val composeUiVersion = "1.3.1"

android {
    namespace = "com.kanastruk.sample.habit"
    compileSdk = 33
    defaultConfig {
        applicationId = "com.kanastruk.sample.habit"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    // https://developer.android.com/jetpack/androidx/releases/compose#versions
    composeOptions {
        kotlinCompilerExtensionVersion = "1.2.0"
    }
    packagingOptions {
        resources.excludes += "META-INF/AL2.0"
        resources.excludes += "META-INF/LGPL2.1"
    }
}

dependencies {
    implementation(project(":libAndroid"))

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")
    implementation("androidx.activity:activity-compose:1.6.1")

    implementation("androidx.navigation:navigation-compose:2.5.3")
    api("androidx.navigation:navigation-fragment-ktx:2.5.3") // https://developer.android.com/guide/navigation/navigation-kotlin-dsl#navgraphbuilder

    implementation("androidx.compose.ui:ui:$composeUiVersion")
    implementation("androidx.compose.ui:ui-tooling-preview:$composeUiVersion")
    implementation("androidx.compose.material:material:1.3.1")
    implementation("androidx.compose.material:material-icons-extended:1.3.1")

    debugImplementation("androidx.compose.ui:ui-tooling:$composeUiVersion")
    debugImplementation("androidx.compose.ui:ui-test-manifest:$composeUiVersion")

    // Simple DI
    implementation("io.insert-koin:koin-android:3.2.0")

    // Square Networking
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.0") // https://github.com/square/okhttp/blob/master/CHANGELOG.md
    implementation ("com.squareup.retrofit2:retrofit:2.9.0") // https://github.com/square/retrofit/blob/master/CHANGELOG.md
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    // Logging
    implementation("com.jakewharton.timber:timber:4.7.1") // https://github.com/JakeWharton/timber/releases

    // Unit tests
    testImplementation("junit:junit:4.13.2")
    testImplementation("io.insert-koin:koin-test:3.2.0")

    // Instrumentation & UI
    androidTestImplementation("androidx.test.ext:junit:1.1.4")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.0")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:$composeUiVersion")

}
