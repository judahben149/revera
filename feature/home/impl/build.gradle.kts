plugins {
    alias(libs.plugins.revera.feature.impl)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.feature.home.api)
        }
    }
}
