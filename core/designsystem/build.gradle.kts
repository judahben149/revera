plugins {
    alias(libs.plugins.revera.cmp.material3)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(libs.compose.material3)
            implementation(libs.materialkolor)
        }
    }
}
