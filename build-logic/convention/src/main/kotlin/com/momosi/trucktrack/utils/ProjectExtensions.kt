package com.momosi.trucktrack.utils

import com.diffplug.gradle.spotless.SpotlessExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType


val Project.spotless: SpotlessExtension
    get() = extensions.getByType(SpotlessExtension::class.java)

val Project.libs
    get(): VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

fun Project.stringProperty(name: String): String? = findProperty(name) as String?
