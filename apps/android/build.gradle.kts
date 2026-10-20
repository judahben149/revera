import java.util.Properties

plugins {
    alias(libs.plugins.revera.android.application)
}

// Release signing: provide keystore.properties at the repo root (git-ignored) or the matching
// REVERA_* environment variables in CI. Without either, release builds are left unsigned.
val signingProperties =
    Properties().apply {
        val file =
            isolated.rootProject.projectDirectory
                .file("keystore.properties")
                .asFile
        if (file.exists()) file.inputStream().use(::load)
    }

fun signingValue(
    key: String,
    env: String,
): String? = signingProperties.getProperty(key) ?: providers.environmentVariable(env).orNull

android {
    namespace = "com.jj.revera"

    defaultConfig {
        applicationId = "com.jj.revera"
    }

    signingConfigs {
        val storeFile = signingValue("storeFile", "REVERA_KEYSTORE_PATH")
        if (storeFile != null) {
            create("release") {
                this.storeFile = file(storeFile)
                storePassword = signingValue("storePassword", "REVERA_KEYSTORE_PASSWORD")
                keyAlias = signingValue("keyAlias", "REVERA_KEY_ALIAS")
                keyPassword = signingValue("keyPassword", "REVERA_KEY_PASSWORD")
            }
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.findByName("release")
        }
    }
}

dependencies {
    implementation(projects.shared)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.profileinstaller)
    implementation(libs.koin.android)
}
