package com.momosi.trucktrack

import com.momosi.trucktrack.utils.commonMainDependencies
import org.gradle.api.Plugin
import org.gradle.api.Project

class FeatureImplPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("trucktrack.library")
            apply("trucktrack.koin")
            apply("trucktrack.compose")
            apply("org.jetbrains.kotlin.plugin.serialization")
        }

        commonMainDependencies {
            implementation(project(":core:ui-library"))
            implementation(project(":core:navigation"))
        }
    }
}
