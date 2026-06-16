package com.momosi.trucktrack

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import com.momosi.trucktrack.utils.api
import com.momosi.trucktrack.utils.libs

class FeatureApiPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("trucktrack.library")
            apply("org.jetbrains.kotlin.plugin.serialization")
        }

        dependencies {
            api(libs.findLibrary("androidx-navigation3-runtime").get())
        }
    }
}
