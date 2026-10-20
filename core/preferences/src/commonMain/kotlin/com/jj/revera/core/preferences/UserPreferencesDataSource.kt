package com.jj.revera.core.preferences

import com.jj.revera.core.model.ThemeMode
import com.jj.revera.core.model.UserPreferences
import com.russhwolf.settings.Settings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Small, synchronous key-value preferences backed by the platform store
 * (SharedPreferences, NSUserDefaults, java.util.prefs, localStorage).
 *
 * Exposes a [StateFlow] seeded from disk, so the very first frame already has the user's theme
 * (no light/dark flash on launch). For large or relational data, add a database module instead.
 */
class UserPreferencesDataSource(
    private val settings: Settings,
) {
    private val state = MutableStateFlow(read())

    val preferences: StateFlow<UserPreferences> = state.asStateFlow()

    fun setThemeMode(mode: ThemeMode) {
        settings.putString(KEY_THEME_MODE, mode.name)
        state.update { it.copy(themeMode = mode) }
    }

    fun setUseDynamicColor(enabled: Boolean) {
        settings.putBoolean(KEY_DYNAMIC_COLOR, enabled)
        state.update { it.copy(useDynamicColor = enabled) }
    }

    private fun read(): UserPreferences {
        val defaults = UserPreferences()
        return UserPreferences(
            themeMode =
                settings
                    .getStringOrNull(KEY_THEME_MODE)
                    ?.let { stored -> ThemeMode.entries.firstOrNull { it.name == stored } }
                    ?: defaults.themeMode,
            useDynamicColor = settings.getBoolean(KEY_DYNAMIC_COLOR, defaults.useDynamicColor),
        )
    }

    private companion object {
        const val KEY_THEME_MODE = "theme_mode"
        const val KEY_DYNAMIC_COLOR = "use_dynamic_color"
    }
}
