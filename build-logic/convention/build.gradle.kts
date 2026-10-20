plugins {
    `kotlin-dsl`
}

group = "com.jj.revera.buildlogic"

kotlin {
    jvmToolchain(
        libs.versions.jvm.toolchain
            .get()
            .toInt(),
    )
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.compose.compiler.gradlePlugin)
}

tasks.validatePlugins {
    enableStricterValidation = true
    failOnWarning = true
}

gradlePlugin {
    plugins {
        register("kmpLibrary") {
            id = "revera.kmp.library"
            implementationClass = "com.jj.revera.buildlogic.KmpLibraryConventionPlugin"
        }
        register("cmpLibrary") {
            id = "revera.cmp.library"
            implementationClass = "com.jj.revera.buildlogic.CmpLibraryConventionPlugin"
        }
        register("cmpMaterial3") {
            id = "revera.cmp.material3"
            implementationClass = "com.jj.revera.buildlogic.CmpMaterial3ConventionPlugin"
        }
        register("cmpAdaptive") {
            id = "revera.cmp.adaptive"
            implementationClass = "com.jj.revera.buildlogic.CmpAdaptiveConventionPlugin"
        }
        register("featureApi") {
            id = "revera.feature.api"
            implementationClass = "com.jj.revera.buildlogic.FeatureApiConventionPlugin"
        }
        register("featureImpl") {
            id = "revera.feature.impl"
            implementationClass = "com.jj.revera.buildlogic.FeatureImplConventionPlugin"
        }
        register("androidApplication") {
            id = "revera.android.application"
            implementationClass = "com.jj.revera.buildlogic.AndroidApplicationConventionPlugin"
        }
        register("buildConfig") {
            id = "revera.build.config"
            implementationClass = "com.jj.revera.buildlogic.BuildConfigConventionPlugin"
        }
    }
}
