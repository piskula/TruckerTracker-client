package com.momosi.trucktrack

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import com.momosi.trucktrack.utils.implementation
import com.momosi.trucktrack.utils.libs

class KoinPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        dependencies {
            implementation(platform(libs.findLibrary("koin.bom").get()))
            implementation(libs.findBundle("koin").get())
        }
    }
}

