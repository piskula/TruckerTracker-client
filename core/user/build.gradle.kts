plugins {
    alias(libs.plugins.trucktrack.library)
    alias(libs.plugins.trucktrack.hilt)
    alias(libs.plugins.trucktrack.ktor)
}

android {
    namespace = "com.momosi.trucktrack.core.user"
}

dependencies {
    implementation(libs.appauth)
    implementation(projects.core.common)
    implementation(libs.jjwt.api)
    runtimeOnly(libs.jjwt.impl)
    runtimeOnly(libs.jjwt.gson)
}
