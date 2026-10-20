package com.jj.revera.core.ui.adaptive

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.layout.positionInWindow
import kotlin.math.roundToInt

/**
 * Splits content around a horizontal hinge: [top] above the fold, [bottom] below it, nothing
 * under the hinge itself. Use it when [LocalReveraWindowInfo] reports [FoldPosture.Tabletop].
 *
 * Must be given bounded height (e.g. `Modifier.fillMaxSize()`).
 */
@Composable
fun TabletopLayout(
    posture: FoldPosture.Tabletop,
    top: @Composable () -> Unit,
    bottom: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    var windowOffsetY by remember { mutableFloatStateOf(0f) }
    Layout(
        contents = listOf(top, bottom),
        modifier = modifier.onPlaced { coordinates -> windowOffsetY = coordinates.positionInWindow().y },
    ) { (topMeasurables, bottomMeasurables), constraints ->
        val height = constraints.maxHeight
        val hingeTop = (posture.hingeBounds.top - windowOffsetY).roundToInt().coerceIn(0, height)
        val hingeBottom = (posture.hingeBounds.bottom - windowOffsetY).roundToInt().coerceIn(hingeTop, height)

        val topPlaceables = topMeasurables.map { it.measure(constraints.copy(minHeight = 0, maxHeight = hingeTop)) }
        val bottomPlaceables =
            bottomMeasurables.map { it.measure(constraints.copy(minHeight = 0, maxHeight = height - hingeBottom)) }

        layout(constraints.maxWidth, height) {
            topPlaceables.forEach { it.place(0, 0) }
            bottomPlaceables.forEach { it.place(0, hingeBottom) }
        }
    }
}
