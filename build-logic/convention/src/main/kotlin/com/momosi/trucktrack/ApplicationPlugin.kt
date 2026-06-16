package com.momosi.trucktrack

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import com.momosi.trucktrack.utils.CompileSdk
import com.momosi.trucktrack.utils.MinSdk
import com.momosi.trucktrack.utils.TargetSdk
import com.momosi.trucktrack.utils.configureKotlin
import com.momosi.trucktrack.utils.implementation
import com.momosi.trucktrack.utils.libs

class ApplicationPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("com.android.application")
        }

        extensions.configure<ApplicationExtension> {
            compileSdk = CompileSdk
            defaultConfig {
                targetSdk = TargetSdk
                minSdk = MinSdk
            }

            buildTypes {
                getByName("release") {
                    isMinifyEnabled = true
                    isShrinkResources = true
                }
                getByName("debug") {
                    isMinifyEnabled = false
                    isShrinkResources = false
                }
            }
        }

        configureKotlin()
    }
}
