package com.jj.revera.buildlogic

import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.getByType

internal const val BASE_PACKAGE = "com.jj.revera"

internal val Project.libs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

internal fun VersionCatalog.lib(alias: String): Provider<MinimalExternalModuleDependency> =
    findLibrary(alias).orElseThrow {
        IllegalArgumentException("Library '$alias' is missing from gradle/libs.versions.toml")
    }

internal fun VersionCatalog.version(alias: String): String =
    findVersion(alias)
        .orElseThrow {
            IllegalArgumentException("Version '$alias' is missing from gradle/libs.versions.toml")
        }.requiredVersion

internal fun VersionCatalog.intVersion(alias: String): Int = version(alias).toInt()

internal fun Project.booleanProperty(name: String): Boolean =
    providers.gradleProperty(name).map(String::toBoolean).getOrElse(false)

internal fun Project.stringProperty(
    name: String,
    default: String,
): String = providers.gradleProperty(name).getOrElse(default)

/**
 * Derives a stable package/namespace from the Gradle path so modules never have to declare one:
 * `:feature:home:impl` -> `com.jj.revera.feature.home.impl`.
 */
internal val Project.reveraNamespace: String
    get() =
        path
            .split(':')
            .filter(String::isNotBlank)
            .joinToString(separator = ".", prefix = "$BASE_PACKAGE.") { segment ->
                segment.replace('-', '_')
            }
