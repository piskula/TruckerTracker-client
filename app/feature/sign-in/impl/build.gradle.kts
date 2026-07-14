plugins {
    alias(libs.plugins.trucktrack.feature.impl)
}

compose.resources {
    packageOfResClass = "com.momosi.trucktrack.feature.signin.impl.resources"
    generateResClass = always
}

kotlin {
    android {
        namespace = "com.momosi.trucktrack.feature.signin.impl"
    }
    sourceSets {
        commonMain.dependencies {
            api(projects.app.feature.signIn.api)
            implementation(projects.app.feature.issues.api)
            implementation(projects.app.core.user)
        }
    }
}
