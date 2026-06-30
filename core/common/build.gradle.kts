plugins {
    alias(libs.plugins.trucktrack.library)
    alias(libs.plugins.trucktrack.koin)
}

android {
    namespace = "com.momosi.trucktrack.core.common"
}

dependencies {
    implementation(libs.kermit)
}
