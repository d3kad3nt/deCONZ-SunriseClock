// Remember that unlike regular Gradle projects, convention plugins in buildSrc do not automatically resolve
// external plugins. We must declare them as dependencies in buildSrc/build.gradle.kts.
// Apply the plugin manually as a workaround with the external plugin version from the version catalog
// specified in implementation dependency artifact in build file.
plugins {
    id("sunriseClock.spotless-convention")
    id("com.android.application")
}

// Access the version catalog.
val libs = extensions.getByType(VersionCatalogsExtension::class.java).named("libs")

android {
    compileSdk = libs.findVersion("compileSdk").get().requiredVersion.toInt()

    defaultConfig {
        minSdk = libs.findVersion("minSdk").get().requiredVersion.toInt()
        targetSdk = libs.findVersion("targetSdk").get().requiredVersion.toInt()

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

        sourceCompatibility = JavaVersion.valueOf(libs.findVersion("java").get().requiredVersion)
        targetCompatibility = JavaVersion.valueOf(libs.findVersion("java").get().requiredVersion)
    }

    testOptions {
        unitTests.all {
            it.useJUnitPlatform {}
        }
    }
}

dependencies {
    // Allow use of newer Java (11) features
    coreLibraryDesugaring(libs.findLibrary("android.tools.desugarjdklibs").get())

    // Tests
    testImplementation(libs.findLibrary("junit5").get())
    testRuntimeOnly(libs.findLibrary("junit5.platformlauncher").get())
    androidTestImplementation(libs.findLibrary("androidx.test.runner").get())
    androidTestImplementation(libs.findLibrary("androidx.test.espresso").get())
}
