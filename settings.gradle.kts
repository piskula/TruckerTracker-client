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
    ":app:app:android",
    ":app:app:shared",
    ":app:core:common",
    ":app:core:ui-library",
    ":app:core:navigation",
    ":app:core:network",
    ":app:core:user",
    ":app:core:vehicle",
    ":app:core:issue",
    ":app:feature:sign-in:api",
    ":app:feature:sign-in:impl",
    ":app:feature:issues:api",
    ":app:feature:issues:impl",
    ":app:feature:profile:api",
    ":app:feature:profile:impl",
)
