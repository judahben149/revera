package com.jj.revera.core.ui.adaptive

import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.material3.adaptive.layout.PaneScaffoldDirective
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Rect
import androidx.window.core.layout.WindowSizeClass

/** Width buckets from the Material 3 window size classes, including Large and Extra-large (desktop/Googlebook). */
enum class WindowWidthClass {
    Compact,
    Medium,
    Expanded,
    Large,
    ExtraLarge,
}

enum class WindowHeightClass {
    Compact,
    Medium,
    Expanded,
}

/** Physical fold state. Only foldable Android devices ever report anything but [Flat]. */
@Immutable
sealed interface FoldPosture {
    data object Flat : FoldPosture

    /** Half-folded with a horizontal hinge, like a laptop — top half for viewing, bottom for controls. */
    data class Tabletop(
        val hingeBounds: Rect,
    ) : FoldPosture

    /** Half-folded with a vertical hinge, like a book — two natural columns. */
    data class Book(
        val hingeBounds: Rect,
    ) : FoldPosture
}

/**
 * Revera's view of the current window. Read it from [LocalReveraWindowInfo] anywhere in the UI;
 * it updates live as the window is resized, folded or moved between displays.
 */
@Immutable
data class ReveraWindowInfo(
    val widthClass: WindowWidthClass,
    val heightClass: WindowHeightClass,
    val posture: FoldPosture,
    /** How many panes side by side the current window supports (1–3). */
    val maxHorizontalPanes: Int,
) {
    val isCompactWidth: Boolean get() = widthClass == WindowWidthClass.Compact
    val showsMultiplePanes: Boolean get() = maxHorizontalPanes > 1

    companion object {
        val Default =
            ReveraWindowInfo(
                widthClass = WindowWidthClass.Compact,
                heightClass = WindowHeightClass.Medium,
                posture = FoldPosture.Flat,
                maxHorizontalPanes = 1,
            )
    }
}

val LocalReveraWindowInfo = compositionLocalOf { ReveraWindowInfo.Default }

/** The adaptive inputs for the current window (size classes up to Extra-large + fold posture). */
@Composable
fun currentReveraAdaptiveInfo(): WindowAdaptiveInfo = currentWindowAdaptiveInfoV2()

/** Pane layout directive shared by every multi-pane scene, so all of them agree on pane counts and hinges. */
@Composable
fun rememberReveraPaneDirective(adaptiveInfo: WindowAdaptiveInfo = currentReveraAdaptiveInfo()): PaneScaffoldDirective =
    remember(adaptiveInfo) { calculatePaneScaffoldDirective(adaptiveInfo) }

/** Provides [LocalReveraWindowInfo] to [content]. Call once, near the root. */
@Composable
fun ProvideReveraWindowInfo(content: @Composable () -> Unit) {
    val adaptiveInfo = currentReveraAdaptiveInfo()
    val directive = rememberReveraPaneDirective(adaptiveInfo)
    val windowInfo = remember(adaptiveInfo, directive) { adaptiveInfo.toReveraWindowInfo(directive) }
    CompositionLocalProvider(LocalReveraWindowInfo provides windowInfo, content = content)
}

internal fun WindowAdaptiveInfo.toReveraWindowInfo(directive: PaneScaffoldDirective): ReveraWindowInfo {
    val sizeClass = windowSizeClass
    val widthClass =
        when {
            sizeClass.isWidthAtLeastBreakpoint(
                WindowSizeClass.WIDTH_DP_EXTRA_LARGE_LOWER_BOUND,
            ) -> WindowWidthClass.ExtraLarge

            sizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_LARGE_LOWER_BOUND) -> WindowWidthClass.Large

            sizeClass.isWidthAtLeastBreakpoint(
                WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND,
            ) -> WindowWidthClass.Expanded

            sizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND) -> WindowWidthClass.Medium

            else -> WindowWidthClass.Compact
        }
    val heightClass =
        when {
            sizeClass.isHeightAtLeastBreakpoint(
                WindowSizeClass.HEIGHT_DP_EXPANDED_LOWER_BOUND,
            ) -> WindowHeightClass.Expanded

            sizeClass.isHeightAtLeastBreakpoint(
                WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND,
            ) -> WindowHeightClass.Medium

            else -> WindowHeightClass.Compact
        }
    val halfOpenedHinge = windowPosture.hingeList.firstOrNull { !it.isFlat }
    val posture =
        when {
            windowPosture.isTabletop && halfOpenedHinge != null -> FoldPosture.Tabletop(halfOpenedHinge.bounds)
            halfOpenedHinge != null && halfOpenedHinge.isVertical -> FoldPosture.Book(halfOpenedHinge.bounds)
            else -> FoldPosture.Flat
        }
    return ReveraWindowInfo(
        widthClass = widthClass,
        heightClass = heightClass,
        posture = posture,
        maxHorizontalPanes = directive.maxHorizontalPartitions,
    )
}
