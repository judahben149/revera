package com.jj.revera.core.preferences.di

import com.jj.revera.core.preferences.UserPreferencesDataSource
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val preferencesModule =
    module {
        includes(platformSettingsModule)
        singleOf(::UserPreferencesDataSource)
    }

/** Provides the platform-backed `com.russhwolf.settings.Settings` instance. */
internal expect val platformSettingsModule: Module

internal const val PREFERENCES_NAME = "com.jj.revera.preferences"
