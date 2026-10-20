package com.jj.revera.core.data.repository

import com.jj.revera.core.model.ThemeMode
import com.jj.revera.core.model.UserPreferences
import com.jj.revera.core.preferences.UserPreferencesDataSource
import kotlinx.coroutines.flow.StateFlow

interface UserPreferencesRepository {
    val preferences: StateFlow<UserPreferences>

    fun setThemeMode(mode: ThemeMode)

    fun setUseDynamicColor(enabled: Boolean)
}

internal class DefaultUserPreferencesRepository(
    private val dataSource: UserPreferencesDataSource,
) : UserPreferencesRepository {
    override val preferences: StateFlow<UserPreferences> = dataSource.preferences

    override fun setThemeMode(mode: ThemeMode) = dataSource.setThemeMode(mode)

    override fun setUseDynamicColor(enabled: Boolean) = dataSource.setUseDynamicColor(enabled)
}
