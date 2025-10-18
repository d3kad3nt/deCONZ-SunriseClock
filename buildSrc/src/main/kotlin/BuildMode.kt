enum class BuildMode {
    Dev,
    Release;

    companion object {
        private fun currentBuildMode(): String {
            return if (System.getenv().containsKey("BUILD_MODE")) {
                System.getenv("BUILD_MODE")
            } else {
                Dev.name
            }
        }

        fun validateBuildMode() {
            val buildModeEnv = currentBuildMode()
            try {

                BuildMode.valueOf(buildModeEnv)
            } catch (_: IllegalArgumentException) {
                error("Invalid build mode: $buildModeEnv (valid modes: ${entries.joinToString(", ")}")
            }
        }

        fun isActive(mode: BuildMode): Boolean {
            validateBuildMode()
            return currentBuildMode() == mode.name
        }
    }
}
