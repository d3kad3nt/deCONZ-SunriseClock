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
    annotationProcessor(libs.androidx.room.compiler)
}
