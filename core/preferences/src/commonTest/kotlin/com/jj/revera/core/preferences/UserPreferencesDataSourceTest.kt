package com.jj.revera.core.preferences

import com.jj.revera.core.model.ThemeMode
import com.jj.revera.core.model.UserPreferences
import com.russhwolf.settings.MapSettings
import kotlin.test.Test
import kotlin.test.assertEquals

class UserPreferencesDataSourceTest {
    @Test
    fun emptyStoreYieldsDefaults() {
        val dataSource = UserPreferencesDataSource(MapSettings())
        assertEquals(UserPreferences(), dataSource.preferences.value)
    }

    @Test
    fun writesArePublishedAndPersisted() {
        val settings = MapSettings()
        UserPreferencesDataSource(settings).apply {
            setThemeMode(ThemeMode.Dark)
            setUseDynamicColor(true)
            assertEquals(UserPreferences(ThemeMode.Dark, useDynamicColor = true), preferences.value)
        }

        // A fresh instance (i.e. the next launch) reads the persisted values.
        val reloaded = UserPreferencesDataSource(settings)
        assertEquals(UserPreferences(ThemeMode.Dark, useDynamicColor = true), reloaded.preferences.value)
    }

    @Test
    fun unknownStoredThemeFallsBackToDefault() {
        val settings = MapSettings("theme_mode" to "Sepia")
        assertEquals(ThemeMode.System, UserPreferencesDataSource(settings).preferences.value.themeMode)
    }
}
