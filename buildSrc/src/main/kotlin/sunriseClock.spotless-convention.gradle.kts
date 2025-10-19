// Remember that unlike regular Gradle projects, convention plugins in buildSrc do not automatically resolve
// external plugins. We must declare them as dependencies in buildSrc/build.gradle.kts.
// Apply the plugin manually as a workaround with the external plugin version from the version catalog
// specified in implementation dependency artifact in build file.
plugins {
    id("com.diffplug.spotless")
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
