package com.jj.revera.buildlogic

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.findByType
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension

/**
 * `revera.android.application` — the Android shell. AGP 9 compiles Kotlin itself (built-in
 * Kotlin), so only the Compose compiler plugin is applied on top.
 */
class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.android.application")
            pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

            val catalog = libs
            val javaVersion = JavaVersion.toVersion(catalog.intVersion("jvm-toolchain"))

            extensions.configure<ApplicationExtension> {
                compileSdk = catalog.intVersion("android-compileSdk")

                defaultConfig {
                    minSdk = catalog.intVersion("android-minSdk")
                    targetSdk = catalog.intVersion("android-targetSdk")
                    versionCode = stringProperty("revera.versionCode", "1").toInt()
                    versionName = stringProperty("revera.versionName", "1.0.0")
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }

                compileOptions {
                    sourceCompatibility = javaVersion
                    targetCompatibility = javaVersion
                }

                buildFeatures {
                    compose = true
                }

                packaging {
                    resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"
                }

                lint {
                    abortOnError = true
                    checkDependencies = true
                    warningsAsErrors = false
                }
            }

            extensions.findByType<KotlinProjectExtension>()?.jvmToolchain(catalog.intVersion("jvm-toolchain"))
        }
    }
}
