package com.momosi.trucktrack

import com.android.build.api.dsl.ApplicationExtension
import com.momosi.trucktrack.utils.stringProperty
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidSigningPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        val keystoreFile = stringProperty("androidKeystoreFile")
        val keystorePassword = stringProperty("androidKeystorePassword")
        val keyAliasProperty = stringProperty("androidKeyAlias")
        val keyPasswordProperty = stringProperty("androidKeyPassword")

        val hasReleaseSigning = listOf(keystoreFile, keystorePassword, keyAliasProperty, keyPasswordProperty)
            .all { !it.isNullOrBlank() }

        extensions.configure<ApplicationExtension> {
            signingConfigs {
                getByName("debug") {
                    storeFile = project.file("keystore/debug.keystore")
                    storePassword = "android"
                    keyAlias = "androiddebugkey"
                    keyPassword = "android"
                }

                if (hasReleaseSigning) {
                    create("release") {
                        storeFile = rootProject.file(keystoreFile!!)
                        storePassword = keystorePassword
                        keyAlias = keyAliasProperty
                        keyPassword = keyPasswordProperty
                    }
                }
            }

            buildTypes {
                if (hasReleaseSigning) {
                    release {
                        signingConfig = signingConfigs.getByName("release")
                    }
                }
            }
        }
    }
}
