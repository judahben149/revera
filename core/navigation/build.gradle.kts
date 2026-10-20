plugins {
    alias(libs.plugins.revera.cmp.library)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(libs.androidx.navigation3.ui)
            api(libs.androidx.lifecycle.viewmodel.navigation3)
            api(libs.kotlinx.serialization.json)
            implementation(libs.androidx.savedstate.compose)
        }
    }
}
