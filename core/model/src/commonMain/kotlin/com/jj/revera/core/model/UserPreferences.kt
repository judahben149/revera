package com.jj.revera.core.model

enum class ThemeMode {
    System,
    Light,
    Dark,
}

data class UserPreferences(
    val themeMode: ThemeMode = ThemeMode.System,
    /** Material You wallpaper-based colors. Only honoured where the platform supports it. */
    val useDynamicColor: Boolean = false,
)
