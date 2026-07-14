package com.momosi.trucktrack.utils

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler


internal fun Project.commonMainDependencies(block: KotlinDependencyHandler.() -> Unit) {
    extensions.configure<KotlinMultiplatformExtension> {
        sourceSets.commonMain.dependencies(block)
    }
}

internal fun Project.androidMainDependencies(block: KotlinDependencyHandler.() -> Unit) {
    extensions.configure<KotlinMultiplatformExtension> {
        sourceSets.androidMain.dependencies(block)
    }
}

internal fun Project.iosMainDependencies(block: KotlinDependencyHandler.() -> Unit) {
    extensions.configure<KotlinMultiplatformExtension> {
        sourceSets.getByName("iosMain").dependencies(block)
    }
}

