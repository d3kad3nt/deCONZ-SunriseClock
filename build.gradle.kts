// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    val navVersion by extra { "2.9.3" }
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.13.0")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$navVersion")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle.kts files
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
