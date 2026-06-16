package com.momosi.trucktrack

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import com.momosi.trucktrack.utils.implementation
import com.momosi.trucktrack.utils.ksp
import com.momosi.trucktrack.utils.libs

class HiltPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("dagger.hilt.android.plugin")
            apply("com.google.devtools.ksp")
        }

        dependencies {
            implementation(libs.findLibrary("hilt").get())
            ksp(libs.findLibrary("hilt.compiler").get())
        }
    }
}
