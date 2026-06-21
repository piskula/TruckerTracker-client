plugins {
    alias(libs.plugins.trucktrack.library)
    alias(libs.plugins.trucktrack.hilt)
    alias(libs.plugins.trucktrack.retrofit)
}

android {
    namespace = "com.momosi.trucktrack.core.network"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.user)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
}
