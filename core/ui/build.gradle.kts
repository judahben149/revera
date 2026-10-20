plugins {
    alias(libs.plugins.revera.cmp.adaptive)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.core.designsystem)
            implementation(projects.core.common)
            api(libs.compose.material3.adaptive)
            api(libs.compose.material3.adaptive.layout)
        }
    }
}
