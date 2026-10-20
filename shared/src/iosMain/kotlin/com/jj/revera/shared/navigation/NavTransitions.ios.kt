package com.jj.revera.shared.navigation

import androidx.navigation3.ui.defaultPopTransitionSpec
import androidx.navigation3.ui.defaultTransitionSpec

internal actual fun <T : Any> reveraTransitionSpec(): SceneTransition<T> = defaultTransitionSpec()

internal actual fun <T : Any> reveraPopTransitionSpec(): SceneTransition<T> = defaultPopTransitionSpec()
