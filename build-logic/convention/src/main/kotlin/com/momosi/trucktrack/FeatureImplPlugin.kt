package com.momosi.trucktrack

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import com.momosi.trucktrack.utils.implementation

class FeatureImplPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("trucktrack.library")
            apply("trucktrack.hilt")
            apply("trucktrack.compose")
        }

        dependencies {
            implementation(project(":core:ui-library"))
            implementation(project(":core:navigation"))
        }
    }
}
