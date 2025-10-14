plugins {
    id("sunriseClock.library-convention")
}

android {
    namespace = "org.d3kad3nt.sunriseClock.util"
}

dependencies {
    implementation(libs.androidx.lifecycle.livedata)
}
