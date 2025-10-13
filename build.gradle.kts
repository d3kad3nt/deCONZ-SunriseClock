// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
    // Loads (but does not apply) plugin once in the root module (Gradle classpaths are hierarchical and
    // submodules won't need to load it again).
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.androidx.navigation.safeargs) apply false
    alias(libs.plugins.androidx.room) apply false
}
