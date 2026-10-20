package com.jj.revera.feature.settings.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jj.revera.core.common.BuildConfig
import com.jj.revera.core.common.platform.PlatformInfo
import com.jj.revera.core.data.repository.UserPreferencesRepository
import com.jj.revera.core.model.ThemeMode
import com.jj.revera.core.model.UserPreferences
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

internal class SettingsViewModel(
    private val preferencesRepository: UserPreferencesRepository,
    private val platformInfo: PlatformInfo,
) : ViewModel() {
    val uiState: StateFlow<SettingsUiState> =
        preferencesRepository.preferences
            .map(::toUiState)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = toUiState(preferencesRepository.preferences.value),
            )

    fun setThemeMode(mode: ThemeMode) = preferencesRepository.setThemeMode(mode)

    fun setUseDynamicColor(enabled: Boolean) = preferencesRepository.setUseDynamicColor(enabled)

    private fun toUiState(preferences: UserPreferences) =
        SettingsUiState(
            themeMode = preferences.themeMode,
            useDynamicColor = preferences.useDynamicColor,
            supportsDynamicColor = platformInfo.supportsDynamicColor,
            version = "${BuildConfig.VERSION_NAME} (${BuildConfig.ENVIRONMENT})",
            platformDescription = platformInfo.description,
        )
}

internal data class SettingsUiState(
    val themeMode: ThemeMode,
    val useDynamicColor: Boolean,
    val supportsDynamicColor: Boolean,
    val version: String,
    val platformDescription: String,
)
