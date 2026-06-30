plugins {
    alias(libs.plugins.trucktrack.library)
    alias(libs.plugins.trucktrack.koin)
    alias(libs.plugins.trucktrack.ktor)
}

kotlin {
    android {
        namespace = "com.momosi.trucktrack.core.user"
    }
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.common)
        }
        androidMain.dependencies {
            implementation(libs.appauth)
            implementation(libs.jjwt.api)
            runtimeOnly(libs.jjwt.impl)
            runtimeOnly(libs.jjwt.gson)
            implementation(libs.androidx.activity.compose)
        }
    }
}
