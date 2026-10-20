package com.jj.revera.core.preferences.di

import android.content.Context
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual val platformSettingsModule: Module =
    module {
        single<Settings> {
            SharedPreferencesSettings(androidContext().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE))
        }
    }
