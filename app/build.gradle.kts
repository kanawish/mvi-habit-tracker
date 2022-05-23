plugins {
    kotlin("android")
    id("com.android.application")
}

val composeUiVersion = "1.2.0-beta02"

android {
    namespace = "com.kanastruk.sample.habit"
    compileSdk = 32
    defaultConfig {
        applicationId = "com.kanastruk.sample.habit"
        minSdk = 24
        targetSdk = 32
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = composeUiVersion
    }
    packagingOptions {
        resources.excludes += "META-INF/AL2.0"
        resources.excludes += "META-INF/LGPL2.1"
    }
}

dependencies {
    implementation(project(":libAndroid"))

    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.1")
    implementation("androidx.activity:activity-compose:1.4.0")

    implementation("androidx.compose.ui:ui:$composeUiVersion")
    implementation("androidx.compose.ui:ui-tooling-preview:$composeUiVersion")
    implementation("androidx.compose.material:material:1.1.1")

    debugImplementation("androidx.compose.ui:ui-tooling:$composeUiVersion")
    debugImplementation("androidx.compose.ui:ui-test-manifest:$composeUiVersion")

    // Simple DI
    implementation("io.insert-koin:koin-android:3.2.0")

    // Square Networking
    debugImplementation ("com.squareup.okhttp3:logging-interceptor:4.9.0") // https://github.com/square/okhttp/blob/master/CHANGELOG.md
    implementation ("com.squareup.retrofit2:retrofit:2.9.0") // https://github.com/square/retrofit/blob/master/CHANGELOG.md
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    // Logging
    implementation("com.jakewharton.timber:timber:4.7.1") // https://github.com/JakeWharton/timber/releases

    // Unit tests
    testImplementation("junit:junit:4.13.2")
    testImplementation("io.insert-koin:koin-test:3.2.0")

    // Instrumentation & UI
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:$composeUiVersion")

}