package com.jj.revera.feature.settings.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.jj.revera.feature.settings.api.SettingsRoute
import com.jj.revera.feature.settings.impl.SettingsRoute as SettingsScreenRoute

fun EntryProviderScope<NavKey>.settingsEntries() {
    entry<SettingsRoute> { SettingsScreenRoute() }
}
