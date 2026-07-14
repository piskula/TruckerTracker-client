plugins {
    alias(libs.plugins.trucktrack.library)
    alias(libs.plugins.trucktrack.koin)
    alias(libs.plugins.trucktrack.ktor)
}

kotlin {
    android {
        namespace = "com.momosi.trucktrack.core.vehicle"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.app.core.common)
            implementation(projects.app.core.network)
        }
    }
}
