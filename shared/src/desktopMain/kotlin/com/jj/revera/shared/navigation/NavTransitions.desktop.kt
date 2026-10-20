package com.jj.revera.shared.navigation

internal actual fun <T : Any> reveraTransitionSpec(): SceneTransition<T> = crossFade()

internal actual fun <T : Any> reveraPopTransitionSpec(): SceneTransition<T> = crossFade()
