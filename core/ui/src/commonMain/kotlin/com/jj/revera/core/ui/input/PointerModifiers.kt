package com.jj.revera.core.ui.input

import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon

/**
 * Shows the hand cursor over custom clickable surfaces (desktop, web, Googlebook trackpads,
 * iPad pointer). Material buttons already handle this; use it for bespoke clickables.
 */
fun Modifier.handCursor(): Modifier = pointerHoverIcon(PointerIcon.Hand)
