// Root build: declares every plugin once (apply false) so all modules share a single classloader,
// and owns repo-wide formatting. Module configuration lives in build-logic convention plugins.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.kmp.library) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.compose.multiplatform) apply false
    alias(libs.plugins.spotless)
}

spotless {
    val ktlintVersion = libs.versions.ktlint.get()
    val composeRules = "io.nlopez.compose.rules:ktlint:${libs.versions.compose.rules.get()}"
    // Explicit source roots: never crawl build/, node_modules/ or Xcode DerivedData.
    kotlin {
        target(
            "apps/*/src/**/*.kt",
            "core/*/src/**/*.kt",
            "feature/*/*/src/**/*.kt",
            "shared/src/**/*.kt",
            "build-logic/convention/src/**/*.kt",
        )
        ktlint(ktlintVersion).customRuleSets(listOf(composeRules))
    }
    kotlinGradle {
        target(
            "*.gradle.kts",
            "apps/*/*.gradle.kts",
            "core/*/*.gradle.kts",
            "feature/*/*/*.gradle.kts",
            "shared/*.gradle.kts",
            "build-logic/*.gradle.kts",
            "build-logic/convention/*.gradle.kts",
        )
        ktlint(ktlintVersion)
    }
    format("misc") {
        target("*.md", "docs/**/*.md", ".gitignore", "gradle/*.toml", ".github/**/*.yml")
        trimTrailingWhitespace()
        endWithNewline()
    }
}
