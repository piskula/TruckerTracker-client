import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.multiplatform.library)
    alias(libs.plugins.kotlin.serialization)
}

group = "com.momosi.trucktrack"

kotlin {
    jvmToolchain(21)
    applyDefaultHierarchyTemplate()

    android {
        namespace = "com.momosi.trucktrack.shared"
    }

    targets.withType<KotlinMultiplatformAndroidLibraryTarget> {
        compileSdk = 37
        minSdk = 28
    }

    jvm()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.datetime)
        }
    }
}
