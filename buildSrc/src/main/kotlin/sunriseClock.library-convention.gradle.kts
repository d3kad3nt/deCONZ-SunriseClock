// Remember that unlike regular Gradle projects, convention plugins in buildSrc do not automatically resolve
// external plugins. We must declare them as dependencies in buildSrc/build.gradle.kts.
// Apply the plugin manually as a workaround with the external plugin version from the version catalog
// specified in implementation dependency artifact in build file.
plugins {
    if (!System.getenv().containsKey("BUILD_MODE") || !System.getenv("BUILD_MODE")
            .equals("release", ignoreCase = true)
    ) {
        id("sunriseClock.spotless-convention")
    }
    id("com.android.library")
}

// Access the version catalog.
val libs = extensions.getByType(VersionCatalogsExtension::class.java).named("libs")

android {
    compileSdk = libs.findVersion("compileSdk").get().requiredVersion.toInt()

    defaultConfig {
        minSdk = libs.findVersion("minSdk").get().requiredVersion.toInt()

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

        sourceCompatibility = JavaVersion.valueOf(libs.findVersion("java").get().requiredVersion)
        targetCompatibility = JavaVersion.valueOf(libs.findVersion("java").get().requiredVersion)
    }
}

dependencies {
    // Allow use of newer Java (11) features
    coreLibraryDesugaring(libs.findLibrary("android.tools.desugarjdklibs").get())

    // Tests
    testImplementation(libs.findLibrary("junit4").get())
    androidTestImplementation(libs.findLibrary("androidx.test.junit4").get())
    androidTestImplementation(libs.findLibrary("androidx.test.espresso").get())
}
