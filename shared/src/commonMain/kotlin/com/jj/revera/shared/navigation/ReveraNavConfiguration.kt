package com.jj.revera.shared.navigation

import com.jj.revera.core.navigation.navSavedStateConfiguration
import com.jj.revera.feature.home.api.homeRoutes
import com.jj.revera.feature.settings.api.settingsRoutes

/** Every feature's routes must be registered here so back stacks can be saved and restored. */
internal val ReveraNavConfiguration =
    navSavedStateConfiguration(
        homeRoutes,
        settingsRoutes,
    )
