plugins {
    `kotlin-dsl`
}

group = "com.momosi.trucktrack.buildlogic"

kotlin {
    jvmToolchain(21)
}

dependencies {
    compileOnly(libs.android.gradle.plugin)
    compileOnly(libs.kotlin.gradle.plugin)
    compileOnly(libs.spotless.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("trucktrack.application") {
            id = "trucktrack.application"
            implementationClass = "com.momosi.trucktrack.ApplicationPlugin"
        }
        register("trucktrack.library") {
            id = "trucktrack.library"
            implementationClass = "com.momosi.trucktrack.LibraryPlugin"
        }
        register("trucktrack.feature.api") {
            id = "trucktrack.feature.api"
            implementationClass = "com.momosi.trucktrack.FeatureApiPlugin"
        }
        register("trucktrack.feature.impl") {
            id = "trucktrack.feature.impl"
            implementationClass = "com.momosi.trucktrack.FeatureImplPlugin"
        }
        register("trucktrack.koin") {
            id = "trucktrack.koin"
            implementationClass = "com.momosi.trucktrack.KoinPlugin"
        }
        register("trucktrack.spotless") {
            id = "trucktrack.spotless"
            implementationClass = "com.momosi.trucktrack.SpotlessPlugin"
        }
        register("trucktrack.compose") {
            id = "trucktrack.compose"
            implementationClass = "com.momosi.trucktrack.ComposePlugin"
        }
        register("trucktrack.ktor") {
            id = "trucktrack.ktor"
            implementationClass = "com.momosi.trucktrack.KtorPlugin"
        }
    }
}
