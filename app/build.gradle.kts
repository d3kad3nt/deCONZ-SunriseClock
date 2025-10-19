plugins {
    id("sunriseClock.application-convention")
    alias(libs.plugins.androidx.navigation.safeargs)
}

android {
    namespace = "org.d3kad3nt.sunriseClock"

    defaultConfig {
        applicationId = "org.d3kad3nt.sunriseClock"
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
}

dependencies {
    // Util module
    implementation(project(":util"))

    // Backend module
    implementation(project(":backend"))

    implementation(libs.bundles.androidx.lifecycle)

    implementation(libs.bundles.androidx.navigation)

    implementation(libs.google.material)
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.drawerlayout)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.cardview)
    implementation(libs.androidx.viewpager)

    implementation(libs.bundles.androidx.appcompat)

    // Additional tests
    androidTestImplementation(libs.androidx.navigation.testing)
}
