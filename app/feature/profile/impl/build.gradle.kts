plugins {
    alias(libs.plugins.trucktrack.feature.impl)
}

compose.resources {
    packageOfResClass = "com.momosi.trucktrack.feature.profile.impl.resources"
    generateResClass = always
}

kotlin {
    android {
        namespace = "com.momosi.trucktrack.feature.profile.impl"
    }
    sourceSets {
        commonMain.dependencies {
            api(projects.app.feature.profile.api)
            implementation(projects.app.feature.signIn.api)
            implementation(projects.app.core.user)
        }
    }
}
