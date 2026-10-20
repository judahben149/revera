rootProject.name = "Revera"

pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    // Lets Gradle provision the JDK toolchain on machines (and CI runners) that don't have it.
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

// --- Platform entry points --------------------------------------------------------------------
// iOS/iPadOS lives in apps/ios (Xcode project) and consumes the :shared framework.
include(":apps:android")
include(":apps:desktop")
include(":apps:web")

// --- Shared application layer (root UI, navigation graph, DI graph, iOS framework) -----------
include(":shared")

// --- Core -------------------------------------------------------------------------------------
include(":core:common")
include(":core:model")
include(":core:data")
include(":core:network")
include(":core:preferences")
include(":core:designsystem")
include(":core:ui")
include(":core:navigation")
include(":core:testing")

// --- Features (api = navigation contract, impl = screens + DI) --------------------------------
include(":feature:home:api")
include(":feature:home:impl")
include(":feature:settings:api")
include(":feature:settings:impl")
