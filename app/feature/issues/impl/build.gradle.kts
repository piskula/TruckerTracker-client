plugins {
    alias(libs.plugins.trucktrack.feature.impl)
}

compose.resources {
    packageOfResClass = "com.momosi.trucktrack.feature.issues.impl.resources"
    generateResClass = always
}

kotlin {
    android {
        namespace = "com.momosi.trucktrack.feature.issues.impl"
    }
    sourceSets {
        commonMain.dependencies {
            api(projects.app.feature.issues.api)
            implementation(projects.app.feature.profile.api)
            implementation(projects.app.core.common)
            implementation(projects.app.core.user)
            implementation(projects.app.core.issue)
            implementation(projects.app.core.vehicle)
            implementation(libs.kotlinx.collections.immutable)
            implementation(libs.coil.compose)
            implementation(libs.filekit.compose)
            implementation(libs.androidx.paging.compose)
        }
    }
}
