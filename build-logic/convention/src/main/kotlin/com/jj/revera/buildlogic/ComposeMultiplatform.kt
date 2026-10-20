package com.jj.revera.buildlogic

import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.compose.resources.ResourcesExtension
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal fun Project.configureComposeMultiplatform() {
    val catalog = libs
    val composeDesktopCurrentOs = extensions.getByType<ComposeExtension>().desktopCurrentOsDependency()

    extensions.configure<KotlinMultiplatformExtension> {
        // Compose resources on Android are packaged as Android assets.
        extensions.getByType<KotlinMultiplatformAndroidLibraryTarget>().androidResources.enable = true

        sourceSets.commonMain.dependencies {
            implementation(catalog.lib("compose-runtime"))
            implementation(catalog.lib("compose-foundation"))
            implementation(catalog.lib("compose-ui"))
            implementation(catalog.lib("compose-components-resources"))
            implementation(catalog.lib("compose-ui-tooling-preview"))
        }
        // Compose UI tests live in desktopTest: headless, fast, no emulator or simulator needed.
        sourceSets.getByName("desktopTest").dependencies {
            implementation(catalog.lib("compose-ui-test"))
            implementation(composeDesktopCurrentOs)
        }
    }

    // Enables Android Studio previews for @Preview functions declared in commonMain.
    dependencies {
        "androidRuntimeClasspath"(catalog.lib("compose-ui-tooling"))
    }

    extensions.getByType<ComposeExtension>().extensions.configure<ResourcesExtension> {
        packageOfResClass = "$reveraNamespace.resources"
    }

    extensions.configure<ComposeCompilerGradlePluginExtension> {
        stabilityConfigurationFiles.add(
            isolated.rootProject.projectDirectory.file("compose-stability.conf"),
        )
        if (booleanProperty("revera.composeCompilerReports")) {
            reportsDestination.set(layout.buildDirectory.dir("compose-reports"))
            metricsDestination.set(layout.buildDirectory.dir("compose-metrics"))
        }
    }
}

@Suppress("DEPRECATION")
private fun ComposeExtension.desktopCurrentOsDependency(): String = dependencies.desktop.currentOs
