plugins {
    id("sunriseClock.library-convention")
}

android {
    namespace = "org.d3kad3nt.sunriseClock.util"

    enableKotlin = false
}

dependencies {
    implementation(libs.androidx.lifecycle.livedata)
}
