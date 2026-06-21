plugins {
    alias(libs.plugins.trucktrack.library)
    alias(libs.plugins.trucktrack.hilt)
    alias(libs.plugins.trucktrack.retrofit)
}

android {
    namespace = "com.momosi.trucktrack.core.issue"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.network)
    implementation(projects.core.vehicle)
    implementation(libs.okhttp)
}
