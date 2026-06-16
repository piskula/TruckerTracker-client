package com.momosi.trucktrack

import com.momosi.trucktrack.utils.implementation
import com.momosi.trucktrack.utils.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class RetrofitPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")

        dependencies {
            implementation(libs.findBundle("retrofit").get())
        }
    }
}

