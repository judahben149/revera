plugins {
    alias(libs.plugins.revera.kmp.library)
}

// Shared test doubles and helpers. Only ever added as a test dependency.
kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.core.model)
            api(projects.core.data)
            api(libs.kotlinx.coroutines.test)
            api(kotlin("test"))
        }
        // kotlin-test needs an explicit framework on JVM targets outside test source sets.
        androidMain.dependencies { api(kotlin("test-junit")) }
        getByName("desktopMain").dependencies { api(kotlin("test-junit")) }
    }
}
