package com.momosi.trucktrack

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import com.momosi.trucktrack.utils.CompileSdk
import com.momosi.trucktrack.utils.MinSdk
import com.momosi.trucktrack.utils.configureKotlin

class LibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("com.android.library")
            apply("trucktrack.spotless")
        }

        extensions.configure<LibraryExtension> {
            compileSdk = CompileSdk
            defaultConfig {
                minSdk = MinSdk
            }
        }

        configureKotlin()
    }
}
