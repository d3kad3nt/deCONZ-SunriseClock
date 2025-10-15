// The buildSrc project does not automatically inherit the version catalog from the main project,
// so additional configuration is required.
dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}
