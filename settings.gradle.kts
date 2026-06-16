pluginManagement {
    includeBuild("build-logic")

    repositories {
        mavenCentral()
        gradlePluginPortal()
        google()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

    repositories {
        google()
        mavenCentral()
    }
}
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "TruckTrack"
include(
    ":app",
    ":core:common",
    ":core:ui-library",
    ":core:navigation",
    ":core:network",
    ":core:user",
    ":core:vehicle",
    ":core:issue",
    ":feature:sign-in:api",
    ":feature:sign-in:impl",
    ":feature:issues:api",
    ":feature:issues:impl",
    ":feature:profile:api",
    ":feature:profile:impl",
)
