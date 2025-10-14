plugins {
    id("sunriseClock.library-convention")
    alias(libs.plugins.androidx.room)
}

android {
    namespace = "org.d3kad3nt.sunriseClock.backend"

    room {
        schemaDirectory("$projectDir/schemas")
    }
}

dependencies {
    // Util module
    implementation(project(":util"))

    implementation(libs.androidx.lifecycle.livedata)

    implementation(libs.bundles.squareup.retrofit)

    implementation(libs.androidx.room.runtime)

    // Preference (TODO: Remove, see #121)
    implementation("androidx.preference:preference:1.2.1")
}
