plugins {
    alias(libs.plugins.trucktrack.library)
    alias(libs.plugins.trucktrack.hilt)
    alias(libs.plugins.trucktrack.retrofit)
}

android {
    namespace = "com.momosi.trucktrack.core.vehicle"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.network)
}
