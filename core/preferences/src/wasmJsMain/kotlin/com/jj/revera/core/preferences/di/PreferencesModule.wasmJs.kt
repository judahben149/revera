package com.jj.revera.core.preferences.di

import com.russhwolf.settings.Settings
import com.russhwolf.settings.StorageSettings
import org.koin.core.module.Module
import org.koin.dsl.module

// Backed by window.localStorage (scoped to the page origin).
internal actual val platformSettingsModule: Module =
    module {
        single<Settings> { StorageSettings() }
    }
