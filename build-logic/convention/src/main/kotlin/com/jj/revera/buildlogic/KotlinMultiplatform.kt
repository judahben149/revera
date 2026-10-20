package com.jj.revera.buildlogic

import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * The single place that decides which platforms Revera ships on.
 *
 * - Android (phones, tablets, foldables, Googlebook/ChromeOS) via the AGP KMP library plugin
 * - Desktop JVM (macOS today; Windows and Linux come for free from the same target)
 * - iOS / iPadOS (Apple-silicon devices + simulators)
 * - Web (Kotlin/Wasm in the browser)
 *
 * To add a target (e.g. `js()` for legacy browsers, or `macosArm64()` for native macOS),
 * add it here and every module picks it up.
 */
internal fun Project.configureKotlinMultiplatform() {
    val catalog = libs
    extensions.configure<KotlinMultiplatformExtension> {
        jvmToolchain(catalog.intVersion("jvm-toolchain"))

        extensions.getByType<KotlinMultiplatformAndroidLibraryTarget>().apply {
            namespace = reveraNamespace
            compileSdk = catalog.intVersion("android-compileSdk")
            minSdk = catalog.intVersion("android-minSdk")
            // Runs commonTest on the JVM as Android unit tests too.
            withHostTest {
                isReturnDefaultValues = true
            }
            lint {
                checkReleaseBuilds = false
            }
        }

        jvm("desktop")

        iosArm64()
        iosSimulatorArm64()

        @OptIn(ExperimentalWasmDsl::class)
        wasmJs {
            browser()
        }

        compilerOptions {
            allWarningsAsErrors.set(booleanProperty("revera.warningsAsErrors"))
            freeCompilerArgs.addAll(
                "-Xexpect-actual-classes",
            )
        }

        sourceSets.commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(catalog.lib("kotlinx-coroutines-test"))
        }
    }
}
