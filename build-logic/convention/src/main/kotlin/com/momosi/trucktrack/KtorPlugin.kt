package com.momosi.trucktrack

import com.momosi.trucktrack.utils.commonMainDependencies
import com.momosi.trucktrack.utils.libs
import org.gradle.api.Plugin
import org.gradle.api.Project

class KtorPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")

        commonMainDependencies {
            implementation(libs.findLibrary("ktor-client-core").get())
            implementation(libs.findLibrary("ktor-client-content-negotiation").get())
            implementation(libs.findLibrary("ktor-client-auth").get())
            implementation(libs.findLibrary("ktor-client-logging").get())
            implementation(libs.findLibrary("ktor-serialization-kotlinx-json").get())
        }
    }
}
