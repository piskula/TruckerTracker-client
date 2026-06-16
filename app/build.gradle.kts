plugins {
    alias(libs.plugins.trucktrack.application)
    alias(libs.plugins.trucktrack.hilt)
    alias(libs.plugins.trucktrack.compose)
    alias(libs.plugins.trucktrack.spotless)
    alias(libs.plugins.parcelize)
}

android {
    namespace = "com.momosi.trucktrack"

    defaultConfig {
        applicationId = "com.momosi.trucktrack"
        proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")

        versionCode = 1
        versionName = "dev"

        manifestPlaceholders["appAuthRedirectScheme"] = "com.momosi.trucktrack"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    lint {
        disable += "MissingTranslation"
    }
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.navigation)
    implementation(projects.core.network)
    implementation(projects.core.uiLibrary)
    implementation(projects.core.user)
    implementation(projects.core.vehicle)
    implementation(projects.core.issue)

    implementation(projects.feature.signIn.impl)
    implementation(projects.feature.myIssues.impl)
    implementation(projects.feature.profile.impl)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.process)
    implementation(libs.androidx.compose.material3)
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.coil.compose)
}
