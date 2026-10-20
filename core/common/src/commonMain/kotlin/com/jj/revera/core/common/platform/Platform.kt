package com.jj.revera.core.common.platform

enum class PlatformFamily {
    Android,
    Ios,
    Desktop,
    Web,
}

enum class HostOs {
    Android,
    Ios,
    IpadOs,
    MacOs,
    Windows,
    Linux,
    Unknown,
}

/** Static facts about the platform the app is running on. */
data class PlatformInfo(
    val family: PlatformFamily,
    val os: HostOs,
    /** Human-readable, e.g. "Android 17 (API 37) · Google Pixel 9 Pro Fold". */
    val description: String,
) {
    /**
     * Apple platforms use ⌘ as the primary shortcut modifier; everything else (including
     * Googlebook/ChromeOS) uses Ctrl.
     */
    val usesMetaAsPrimaryModifier: Boolean
        get() = os == HostOs.MacOs || os == HostOs.Ios || os == HostOs.IpadOs

    /** Wallpaper-derived Material You color schemes (Android 12+). */
    val supportsDynamicColor: Boolean
        get() = family == PlatformFamily.Android && dynamicColorAvailable()
}

expect fun currentPlatform(): PlatformInfo

internal expect fun dynamicColorAvailable(): Boolean
