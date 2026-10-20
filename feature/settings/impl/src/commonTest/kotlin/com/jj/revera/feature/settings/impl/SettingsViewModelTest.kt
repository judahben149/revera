package com.jj.revera.feature.settings.impl

import app.cash.turbine.test
import com.jj.revera.core.common.platform.HostOs
import com.jj.revera.core.common.platform.PlatformFamily
import com.jj.revera.core.common.platform.PlatformInfo
import com.jj.revera.core.model.ThemeMode
import com.jj.revera.core.testing.FakeUserPreferencesRepository
import com.jj.revera.core.testing.MainDispatcherTest
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class SettingsViewModelTest : MainDispatcherTest() {
    private val platform = PlatformInfo(PlatformFamily.Desktop, HostOs.Linux, "Test OS")

    @Test
    fun reflectsAndUpdatesPreferences() =
        runTest {
            val repository = FakeUserPreferencesRepository()
            val viewModel = SettingsViewModel(repository, platform)

            viewModel.uiState.test {
                assertEquals(ThemeMode.System, awaitItem().themeMode)
                viewModel.setThemeMode(ThemeMode.Dark)
                assertEquals(ThemeMode.Dark, awaitItem().themeMode)
            }
        }

    @Test
    fun dynamicColorIsHiddenWherePlatformLacksIt() {
        val viewModel = SettingsViewModel(FakeUserPreferencesRepository(), platform)
        assertFalse(viewModel.uiState.value.supportsDynamicColor)
        assertEquals("Test OS", viewModel.uiState.value.platformDescription)
    }
}
