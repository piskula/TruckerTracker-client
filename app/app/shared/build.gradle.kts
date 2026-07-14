plugins {
    alias(libs.plugins.trucktrack.library)
    alias(libs.plugins.trucktrack.compose)
    alias(libs.plugins.trucktrack.koin)
}

compose.resources {
    packageOfResClass = "com.momosi.trucktrack.app.resources"
}

kotlin {
    android {
        namespace = "com.momosi.trucktrack.app"
    }

    iosArm64 {
        binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }
    iosSimulatorArm64 {
        binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.coil.singleton)
            implementation(libs.coil.network.ktor)
            implementation(projects.app.core.common)
            implementation(projects.app.core.navigation)
            implementation(projects.app.core.network)
            implementation(projects.app.core.uiLibrary)
            implementation(projects.app.core.user)
            implementation(projects.app.core.vehicle)
            implementation(projects.app.core.issue)

            implementation(projects.app.feature.signIn.api)
            implementation(projects.app.feature.signIn.impl)
            implementation(projects.app.feature.issues.api)
            implementation(projects.app.feature.issues.impl)
            implementation(projects.app.feature.profile.impl)
        }
    }
}
