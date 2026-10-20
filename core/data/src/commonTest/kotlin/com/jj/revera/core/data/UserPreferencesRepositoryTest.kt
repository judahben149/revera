package com.jj.revera.core.data

import app.cash.turbine.test
import com.jj.revera.core.data.repository.DefaultUserPreferencesRepository
import com.jj.revera.core.model.ThemeMode
import com.jj.revera.core.preferences.UserPreferencesDataSource
import com.russhwolf.settings.MapSettings
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class UserPreferencesRepositoryTest {
    @Test
    fun themeChangesAreObservable() =
        runTest {
            val repository = DefaultUserPreferencesRepository(UserPreferencesDataSource(MapSettings()))

            repository.preferences.test {
                assertEquals(ThemeMode.System, awaitItem().themeMode)
                repository.setThemeMode(ThemeMode.Light)
                assertEquals(ThemeMode.Light, awaitItem().themeMode)
            }
        }
}
