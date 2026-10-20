import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    alias(libs.plugins.revera.cmp.adaptive)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    // The iOS app links this module as a static framework: `import ReveraShared` in Swift.
    targets.withType<KotlinNativeTarget>().configureEach {
        binaries.framework {
            baseName = "ReveraShared"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(projects.core.common)
            api(projects.core.ui)
            implementation(projects.core.model)
            implementation(projects.core.data)
            implementation(projects.core.network)
            implementation(projects.core.designsystem)
            implementation(projects.core.navigation)

            implementation(projects.feature.home.impl)
            implementation(projects.feature.settings.impl)

            implementation(libs.compose.material3.navigation.suite)
            implementation(libs.compose.material3.adaptive.navigation3)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.androidx.lifecycle.viewmodel.compose)
            api(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
        }
        commonTest.dependencies {
            implementation(projects.core.testing)
            implementation(libs.koin.test)
            implementation(libs.multiplatform.settings)
            implementation(libs.multiplatform.settings.test)
        }
    }
}
