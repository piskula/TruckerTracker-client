plugins {
    alias(libs.plugins.trucktrack.library)
    alias(libs.plugins.trucktrack.koin)
}

kotlin {
    android {
        namespace = "com.momosi.trucktrack.core.common"
    }
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kermit)
            implementation(libs.kotlinx.datetime)
        }
    }
}
