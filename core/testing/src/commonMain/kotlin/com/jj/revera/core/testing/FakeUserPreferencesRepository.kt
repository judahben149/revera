package com.jj.revera.core.testing

import com.jj.revera.core.data.repository.UserPreferencesRepository
import com.jj.revera.core.model.ThemeMode
import com.jj.revera.core.model.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class FakeUserPreferencesRepository(
    initial: UserPreferences = UserPreferences(),
) : UserPreferencesRepository {
    private val state = MutableStateFlow(initial)

    override val preferences: StateFlow<UserPreferences> = state

    override fun setThemeMode(mode: ThemeMode) = state.update { it.copy(themeMode = mode) }

    override fun setUseDynamicColor(enabled: Boolean) = state.update { it.copy(useDynamicColor = enabled) }
}
