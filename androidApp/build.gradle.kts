plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.trucktrack.spotless)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.momosi.trucktrack"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.momosi.trucktrack"
        targetSdk = 37
        minSdk = 28
        proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")

        versionCode = 1
        versionName = "dev"

        manifestPlaceholders["appAuthRedirectScheme"] = "com.momosi.trucktrack"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
        }
        debug {
            isMinifyEnabled = false
            isShrinkResources = false
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    lint {
        disable += "MissingTranslation"
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll(
            "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
            "-opt-in=kotlinx.coroutines.FlowPreview",
            "-opt-in=kotlinx.serialization.ExperimentalSerializationApi",
        )
    }
}

dependencies {
    implementation(projects.composeApp)
    implementation(projects.core.common)
    implementation(projects.core.network)

    implementation(libs.androidx.activity.compose)
    implementation(libs.coil.compose)
    implementation(libs.coil.network.ktor)
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
}
