plugins {
    alias(libs.plugins.trucktrack.feature.impl)
}

android {
    namespace = "com.momosi.trucktrack.feature.profile.impl"
}

dependencies {
    api(projects.feature.profile.api)
    implementation(projects.feature.signIn.api)
    implementation(projects.core.user)
}

