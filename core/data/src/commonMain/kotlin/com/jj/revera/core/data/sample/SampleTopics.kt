package com.jj.revera.core.data.sample

import com.jj.revera.core.model.Topic

/** Placeholder content that doubles as a tour of what the Revera foundation supports. */
internal object SampleTopics {
    val all: List<Topic> =
        listOf(
            Topic(
                id = "adaptive",
                title = "Adaptive by default",
                summary = "One UI that reflows across compact, medium, expanded, large and extra-large windows.",
                body =
                    "Layout decisions read window size classes — never the physical screen. The navigation " +
                        "suite switches between a bottom bar, a rail and a drawer, and Home becomes a " +
                        "list-detail layout as soon as there is room for two panes.\n\nResize the window " +
                        "(desktop, web, Googlebook, iPad Stage Manager) and watch it adapt live.",
                readingMinutes = 2,
            ),
            Topic(
                id = "foldables",
                title = "Foldables & postures",
                summary = "Hinge-aware panes, book posture and tabletop mode.",
                body =
                    "Pane layouts avoid separating hinges, so content never splits across a fold. In " +
                        "tabletop posture (half-folded, hinge horizontal) this detail screen splits into a " +
                        "header above the fold and content below it.\n\nPosture comes from Jetpack " +
                        "WindowManager through Material 3 adaptive, so it is available in shared code.",
                readingMinutes = 2,
            ),
            Topic(
                id = "googlebook",
                title = "Googlebook & desktop windowing",
                summary = "Free-form windows, keyboard, trackpad and multi-window on Android laptops.",
                body =
                    "The Android app is fully resizable, does not require a touchscreen, declares a " +
                        "sensible default window size, handles window resizes without restarting, and " +
                        "publishes its keyboard shortcuts to the system Keyboard Shortcuts Helper.\n\n" +
                        "Hover states and pointer icons make mouse and trackpad input feel native.",
                readingMinutes = 3,
            ),
            Topic(
                id = "keyboard",
                title = "Keyboard first",
                summary = "Shortcuts that follow each platform's conventions.",
                body =
                    "Shortcuts are declared once and map to ⌘ on Apple platforms and Ctrl elsewhere. " +
                        "Try ⌘/Ctrl + 1 for Home, ⌘/Ctrl + , for Settings and Esc to go back.\n\nOn " +
                        "macOS, Windows and Linux they also appear in the native menu bar.",
                readingMinutes = 1,
            ),
            Topic(
                id = "ipad",
                title = "iPhone & iPadOS",
                summary = "Universal app with Split View, Slide Over and Stage Manager support.",
                body =
                    "The iOS app targets iPhone and iPad, supports every orientation on iPad, and opts " +
                        "into multitasking, so Revera can share the screen with other apps and be freely " +
                        "resized. Hardware keyboards get the same shortcuts as desktop.",
                readingMinutes = 2,
            ),
            Topic(
                id = "desktop",
                title = "macOS, Windows & Linux",
                summary = "Native installers for every desktop: DMG/PKG, MSI/EXE, DEB/RPM.",
                body =
                    "The desktop app runs on the JVM with Compose, so the same build works on all three " +
                        "operating systems. Packaging is configured for each one; run " +
                        "packageDistributionForCurrentOS on the target OS to produce its installer.",
                readingMinutes = 2,
            ),
            Topic(
                id = "web",
                title = "Web with Kotlin/Wasm",
                summary = "The same Compose UI, compiled to WebAssembly for modern browsers.",
                body =
                    "The web app renders the shared UI on a canvas via Kotlin/Wasm and stores " +
                        "preferences in localStorage. Every evergreen browser supports WasmGC.",
                readingMinutes = 1,
            ),
            Topic(
                id = "architecture",
                title = "Built to scale",
                summary = "Convention plugins, feature api/impl modules, Koin, Navigation 3.",
                body =
                    "Features are split into api (navigation contract) and impl (UI + ViewModels + DI) " +
                        "modules, so they never depend on each other's internals. Build configuration " +
                        "lives in convention plugins, versions in one catalog, and every platform target " +
                        "is declared in exactly one place.",
                readingMinutes = 3,
            ),
        )
}
