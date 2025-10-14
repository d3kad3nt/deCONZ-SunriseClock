// Remember that unlike regular Gradle projects, convention plugins in buildSrc do not automatically resolve
// external plugins. We must declare them as dependencies in buildSrc/build.gradle.kts.
// Apply the plugin manually as a workaround with the external plugin version from the version catalog
// specified in implementation dependency artifact in build file.
plugins {
    id("sunriseClock.spotless-convention")
    id("com.android.application")
}

android {
    compileSdk = 36

    defaultConfig {
        minSdk = 21
        targetSdk = 36

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    testOptions {
        unitTests.all {
            it.useJUnitPlatform {}
        }
    }
}

// Access the version catalog.
val libs = extensions.getByType(VersionCatalogsExtension::class.java).named("libs")

dependencies {
    // Allow use of newer Java (11) features
    coreLibraryDesugaring(libs.findLibrary("android.tools.desugarjdklibs").get())

    // Tests
    testImplementation(libs.findLibrary("junit5").get())
    testRuntimeOnly(libs.findLibrary("junit5.platformlauncher").get())
    androidTestImplementation(libs.findLibrary("androidx.test.runner").get())
    androidTestImplementation(libs.findLibrary("androidx.test.espresso").get())
}
