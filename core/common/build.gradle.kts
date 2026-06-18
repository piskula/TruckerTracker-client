plugins {
    alias(libs.plugins.trucktrack.library)
    alias(libs.plugins.trucktrack.hilt)
}

android {
    namespace = "com.momosi.trucktrack.core.common"
}

dependencies {
    implementation(libs.timber)
}
