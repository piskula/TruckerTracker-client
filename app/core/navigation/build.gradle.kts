plugins {
    alias(libs.plugins.trucktrack.library)
    alias(libs.plugins.trucktrack.compose)
}

kotlin {
    android {
        namespace = "com.momosi.trucktrack.core.navigation"
    }
}
