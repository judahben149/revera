import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    jvmToolchain(
        libs.versions.jvm.toolchain
            .get()
            .toInt(),
    )
}

dependencies {
    implementation(projects.shared)
    implementation(compose.desktop.currentOs)
    implementation(libs.compose.components.resources)
    implementation(libs.kotlinx.coroutines.swing)
}

compose.resources {
    packageOfResClass = "com.jj.revera.desktop.resources"
}

val appVersion = providers.gradleProperty("revera.versionName").get()

compose.desktop {
    application {
        mainClass = "com.jj.revera.desktop.MainKt"
        jvmArgs += listOf("-Xms256m", "-Xmx1g")

        nativeDistributions {
            // Build each installer on its own OS (jpackage can't cross-compile):
            //   macOS → DMG/PKG, Windows → MSI/EXE, Linux → DEB/RPM.
            targetFormats(
                TargetFormat.Dmg,
                TargetFormat.Pkg,
                TargetFormat.Msi,
                TargetFormat.Exe,
                TargetFormat.Deb,
                TargetFormat.Rpm,
            )
            packageName = "Revera"
            packageVersion = appVersion
            description = "Revera"
            vendor = "Revera"
            copyright = "© 2026 Revera"
            // JDK modules the app needs at runtime (verify with ./gradlew :apps:desktop:suggestRuntimeModules).
            modules("java.instrument", "java.management", "java.net.http", "java.prefs", "jdk.unsupported")

            macOS {
                bundleID = "com.jj.revera"
                dockName = "Revera"
                appCategory = "public.app-category.productivity"
                iconFile.set(project.file("icons/revera.icns"))
                // Signing/notarization: set these in ~/.gradle/gradle.properties or CI, never in the repo.
                //   compose.desktop.mac.sign=true
                //   compose.desktop.mac.signing.identity=Developer ID Application: …
                //   compose.desktop.mac.notarization.appleID / .password / .teamID
            }
            windows {
                iconFile.set(project.file("icons/revera.ico"))
                menuGroup = "Revera"
                // Must never change once shipped: it's how Windows upgrades an existing install.
                upgradeUuid = "4c1f6b2e-7a39-4d6e-9b5a-3e8f0c2d71a4"
                perUserInstall = true
                shortcut = true
                dirChooser = true
            }
            linux {
                iconFile.set(project.file("icons/revera.png"))
                packageName = "revera"
                appCategory = "Utility"
                menuGroup = "Utility"
                shortcut = true
                debMaintainer = "judahben149@gmail.com"
            }
        }

        buildTypes.release.proguard {
            configurationFiles.from(project.file("proguard-rules.pro"))
            obfuscate.set(false)
        }
    }
}
