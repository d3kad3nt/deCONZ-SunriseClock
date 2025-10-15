pluginManagement {
    // The pluginManagement.repositories block configures the repositories Gradle uses to search or
    // download the Gradle plugins and their transitive dependencies.
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    // The dependencyResolutionManagement.repositories block is where you configure the repositories
    // and dependencies used by all modules in your project, such as libraries that you are using to
    // create your application. However, you should configure module-specific dependencies in each
    // module-level build.gradle file.
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

include(":app")
include(":util")
include(":backend")
