package com.momosi.trucktrack

import com.momosi.trucktrack.utils.androidMainDependencies
import com.momosi.trucktrack.utils.commonMainDependencies
import com.momosi.trucktrack.utils.libs
import org.gradle.api.Plugin
import org.gradle.api.Project

class KoinPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        commonMainDependencies {
            implementation(libs.findLibrary("koin-core").get())
        }
        androidMainDependencies {
            implementation(libs.findLibrary("koin-android").get())
            implementation(libs.findLibrary("koin-androidx-compose").get())
            implementation(libs.findLibrary("koin-compose-viewmodel").get())
        }
    }
}
