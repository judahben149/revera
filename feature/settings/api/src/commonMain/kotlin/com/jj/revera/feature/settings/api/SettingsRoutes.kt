package com.jj.revera.feature.settings.api

import androidx.navigation3.runtime.NavKey
import com.jj.revera.core.navigation.NavKeyRegistration
import kotlinx.serialization.Serializable

@Serializable
data object SettingsRoute : NavKey

val settingsRoutes: NavKeyRegistration = {
    subclass(SettingsRoute::class, SettingsRoute.serializer())
}
