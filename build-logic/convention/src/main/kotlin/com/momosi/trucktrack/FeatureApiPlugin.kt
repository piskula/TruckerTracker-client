package com.momosi.trucktrack

import com.momosi.trucktrack.utils.commonMainDependencies
import com.momosi.trucktrack.utils.libs
import org.gradle.api.Plugin
import org.gradle.api.Project

class FeatureApiPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("trucktrack.library")
            apply("org.jetbrains.kotlin.plugin.serialization")
        }

        commonMainDependencies {
            api(libs.findLibrary("androidx-navigation3-runtime").get())
        }
    }
}
