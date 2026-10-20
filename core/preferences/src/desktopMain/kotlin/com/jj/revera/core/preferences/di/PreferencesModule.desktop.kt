package com.jj.revera.core.preferences.di

import com.russhwolf.settings.PreferencesSettings
import com.russhwolf.settings.Settings
import org.koin.core.module.Module
import org.koin.dsl.module
import java.util.prefs.Preferences

internal actual val platformSettingsModule: Module =
    module {
        single<Settings> {
            PreferencesSettings(Preferences.userRoot().node(PREFERENCES_NAME.replace('.', '/')))
        }
    }
