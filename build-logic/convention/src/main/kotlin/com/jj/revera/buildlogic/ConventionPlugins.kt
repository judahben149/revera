package com.jj.revera.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/** `revera.kmp.library` — a pure Kotlin Multiplatform module on every Revera target. No Compose. */
class KmpLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) =
        with(target) {
            pluginManager.apply("org.jetbrains.kotlin.multiplatform")
            pluginManager.apply("com.android.kotlin.multiplatform.library")
            configureKotlinMultiplatform()
        }
}

/** `revera.cmp.library` — a Kotlin Multiplatform module with Compose UI and Compose resources. */
class CmpLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) =
        with(target) {
            pluginManager.apply("revera.kmp.library")
            pluginManager.apply("org.jetbrains.compose")
            pluginManager.apply("org.jetbrains.kotlin.plugin.compose")
            configureComposeMultiplatform()
        }
}

/**
 * `revera.cmp.material3` — a Compose module built on Material 3. Opts in to the Material 3 APIs
 * still marked experimental upstream (expressive components) so call sites stay clean.
 */
class CmpMaterial3ConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) =
        with(target) {
            pluginManager.apply("revera.cmp.library")
            val catalog = libs
            extensions.configure<KotlinMultiplatformExtension> {
                compilerOptions.optIn.addAll(
                    "androidx.compose.material3.ExperimentalMaterial3Api",
                    "androidx.compose.material3.ExperimentalMaterial3ExpressiveApi",
                )
                sourceSets.commonMain.dependencies {
                    implementation(catalog.lib("compose-material3"))
                }
            }
        }
}

/**
 * `revera.cmp.adaptive` — a Material 3 module that lays out for window size classes, panes and
 * fold postures (Material 3 adaptive). Revera relies on these still-experimental APIs for every
 * multi-pane and foldable layout, so the opt-in lives here.
 */
class CmpAdaptiveConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) =
        with(target) {
            pluginManager.apply("revera.cmp.material3")
            val catalog = libs
            extensions.configure<KotlinMultiplatformExtension> {
                compilerOptions.optIn.add("androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi")
                sourceSets.commonMain.dependencies {
                    implementation(catalog.lib("compose-material3-adaptive"))
                    implementation(catalog.lib("compose-material3-adaptive-layout"))
                }
            }
        }
}

/**
 * `revera.feature.api` — a feature's public contract: its navigation keys (and, later, any
 * interfaces other features may call). Feature impl modules may depend on other features' api
 * modules, never on another impl.
 */
class FeatureApiConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) =
        with(target) {
            pluginManager.apply("revera.kmp.library")
            pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")
            extensions.configure<KotlinMultiplatformExtension> {
                sourceSets.commonMain.dependencies {
                    api(project(":core:navigation"))
                    implementation(libs.lib("kotlinx-serialization-json"))
                }
            }
        }
}

/** `revera.feature.impl` — a feature's screens, ViewModels, navigation entries and DI module. */
class FeatureImplConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) =
        with(target) {
            pluginManager.apply("revera.cmp.adaptive")
            pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")
            val catalog = libs
            extensions.configure<KotlinMultiplatformExtension> {
                sourceSets.commonMain.dependencies {
                    implementation(project(":core:common"))
                    implementation(project(":core:model"))
                    implementation(project(":core:data"))
                    implementation(project(":core:designsystem"))
                    implementation(project(":core:ui"))
                    implementation(project(":core:navigation"))

                    implementation(catalog.lib("compose-material3-adaptive-navigation3"))
                    implementation(catalog.lib("androidx-lifecycle-runtime-compose"))
                    implementation(catalog.lib("androidx-lifecycle-viewmodel-compose"))
                    implementation(catalog.lib("koin-core"))
                    implementation(catalog.lib("koin-compose"))
                    implementation(catalog.lib("koin-compose-viewmodel"))
                    implementation(catalog.lib("kotlinx-collections-immutable"))
                    implementation(catalog.lib("kotlinx-serialization-json"))
                }
                sourceSets.commonTest.dependencies {
                    implementation(project(":core:testing"))
                    implementation(catalog.lib("turbine"))
                }
            }
        }
}
