plugins {
    alias(libs.plugins.trucktrack.feature.impl)
}

android {
    namespace = "com.momosi.trucktrack.feature.signin.impl"
}

dependencies {
    api(projects.feature.signIn.api)
    implementation(projects.feature.issues.api)
    implementation(projects.core.user)
}
