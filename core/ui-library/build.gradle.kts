plugins {
    alias(libs.plugins.trucktrack.library)
    alias(libs.plugins.trucktrack.compose)
}

android {
    namespace = "com.momosi.trucktrack.core.uilibrary"
}

dependencies {
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.text.google.fonts)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.coil.core)
    implementation(libs.coil.compose)
    implementation(libs.androidx.navigation3.ui)
    implementation(projects.core.common)
}
