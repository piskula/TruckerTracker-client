rootProject.name = "TruckerTracker"

include("module-api")
include("module-server")

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}
