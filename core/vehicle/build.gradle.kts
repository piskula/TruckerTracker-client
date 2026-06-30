plugins {
    alias(libs.plugins.trucktrack.library)
    alias(libs.plugins.trucktrack.koin)
    alias(libs.plugins.trucktrack.ktor)
}

android {
    namespace = "com.momosi.trucktrack.core.vehicle"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.network)
}
