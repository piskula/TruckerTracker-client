plugins {
    alias(libs.plugins.trucktrack.library)
    alias(libs.plugins.trucktrack.koin)
    alias(libs.plugins.trucktrack.ktor)
}

kotlin {
    android {
        namespace = "com.momosi.trucktrack.core.issue"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.app.core.common)
            implementation(projects.app.core.network)
            implementation(projects.app.core.vehicle)
            implementation(libs.androidx.paging.common)
        }
    }
}
