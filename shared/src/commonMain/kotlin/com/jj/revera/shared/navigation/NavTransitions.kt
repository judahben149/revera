package com.jj.revera.shared.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.navigation3.scene.Scene

/**
 * Scene transitions for NavDisplay.
 *
 * Android and iOS keep Navigation 3's native defaults (predictive back, iOS slide). Web and
 * desktop default to *instant* transitions, which race Navigation 3's scene bookkeeping and can
 * leave a re-entered list-detail scene blank (Settings → Home on a two-pane window). A brief
 * cross-fade avoids that and reads better on large screens anyway. Covered by AppNavigationTest.
 */
internal typealias SceneTransition<T> = AnimatedContentTransitionScope<Scene<T>>.() -> ContentTransform

internal expect fun <T : Any> reveraTransitionSpec(): SceneTransition<T>

internal expect fun <T : Any> reveraPopTransitionSpec(): SceneTransition<T>

private const val CROSS_FADE_MILLIS = 150

internal fun <T : Any> crossFade(): SceneTransition<T> =
    { fadeIn(tween(CROSS_FADE_MILLIS)) togetherWith fadeOut(tween(CROSS_FADE_MILLIS)) }
