package com.jj.revera.core.preferences.di

import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.Settings
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.Foundation.NSUserDefaults

internal actual val platformSettingsModule: Module =
    module {
        single<Settings> { NSUserDefaultsSettings(NSUserDefaults.standardUserDefaults) }
    }
