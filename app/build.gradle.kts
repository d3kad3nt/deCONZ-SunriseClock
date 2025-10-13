plugins {
    alias(libs.plugins.diffplug.spotless)
    alias(libs.plugins.android.application)
    alias(libs.plugins.androidx.navigation.safeargs)
}

android {
    namespace = "org.d3kad3nt.sunriseClock"
    compileSdk = 36

    defaultConfig {
        applicationId = "org.d3kad3nt.sunriseClock"
        minSdk = 21
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
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

dependencies {
    // Util module
    implementation(project(":util"))

    // Backend module
    implementation(project(":backend"))

    coreLibraryDesugaring(libs.android.tools.desugarjdklibs)

    implementation(libs.bundles.lifecycle)

    implementation(libs.bundles.navigation)

    implementation(libs.google.material)
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.drawerlayout)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.cardview)
    implementation(libs.androidx.viewpager)

    implementation(libs.bundles.appcompat)

    // Preference (TODO: Remove, see #121)
    implementation("androidx.preference:preference:1.2.1")

    // Tests
    testImplementation(libs.junit5)
    testRuntimeOnly(libs.junit5.platformlauncher)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.test.espresso)
    androidTestImplementation(libs.androidx.navigation.testing)
}

spotless {
    format("misc") {
        // define the files to apply `misc` to
        target("*.gradle", ".gitattributes", ".gitignore")

        // define the steps to apply to those files
        trimTrailingWhitespace()
        // leadingSpacesToTabs() // or leadingTabsToSpaces. Takes an integer argument if you don"t like 4
        endWithNewline()
    }
    java {
        target("src/*/java/**/*.java")
        // Use the default importOrder configuration
        importOrder()
        removeUnusedImports()
        // apply a specific flavor of palantir-java-format
        leadingSpacesToTabs()
        palantirJavaFormat("2.61.0").formatJavadoc(true)
    }
}
