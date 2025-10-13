plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.androidx.room)
}

android {
    namespace = "org.d3kad3nt.sunriseClock.backend"
    compileSdk = 36

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true

        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    room {
        schemaDirectory("$projectDir/schemas")
    }
}

dependencies {
    // Util module
    implementation(project(":util"))

    coreLibraryDesugaring(libs.desugar.jdk.libs)

    implementation(libs.lifecycle.livedata)

    implementation(libs.bundles.retrofit)

    implementation(libs.room.runtime)

    // Preference (TODO: Remove, see #121)
    implementation("androidx.preference:preference:1.2.1")

    testImplementation(libs.junit4)
    androidTestImplementation(libs.androidx.test.junit4)
    androidTestImplementation(libs.androidx.test.espresso)
}
