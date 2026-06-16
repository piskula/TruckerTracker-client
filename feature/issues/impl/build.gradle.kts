plugins {
    alias(libs.plugins.trucktrack.feature.impl)
}

android {
    namespace = "com.momosi.trucktrack.feature.issues.impl"
}

dependencies {
    api(projects.feature.issues.api)
    implementation(projects.feature.profile.api)
    implementation(projects.core.common)
    implementation(projects.core.user)
    implementation(projects.core.issue)
    implementation(projects.core.vehicle)
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.coil.compose)
}
