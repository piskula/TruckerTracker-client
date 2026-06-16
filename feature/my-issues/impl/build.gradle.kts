plugins {
    alias(libs.plugins.trucktrack.feature.impl)
}

android {
    namespace = "com.momosi.trucktrack.feature.myissues.impl"
}

dependencies {
    api(projects.feature.myIssues.api)
    implementation(projects.core.user)
}

