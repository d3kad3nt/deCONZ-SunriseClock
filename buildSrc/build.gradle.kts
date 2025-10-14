plugins {
    // Enables use of precompiled script plugins as convention plugins.
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
}

// Access version catalog in buildSrc build file for dependencies.
// Versioned dependencies can be used in convention plugins.
dependencies {
    implementation(plugin(libs.plugins.diffplug.spotless))
    implementation(plugin(libs.plugins.android.library))
    implementation(plugin(libs.plugins.android.application))
}

// Helper function that transforms a Gradle plugin alias from a
// version catalog into a valid dependency notation for buildSrc.
fun DependencyHandlerScope.plugin(plugin: Provider<PluginDependency>) =
    plugin.map { "${it.pluginId}:${it.pluginId}.gradle.plugin:${it.version}" }
