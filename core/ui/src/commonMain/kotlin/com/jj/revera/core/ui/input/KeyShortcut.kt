package com.jj.revera.core.ui.input

import androidx.compose.runtime.Immutable
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isAltPressed
import androidx.compose.ui.input.key.isCtrlPressed
import androidx.compose.ui.input.key.isMetaPressed
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type

/**
 * A platform-neutral keyboard shortcut. [primary] means ⌘ on Apple platforms and Ctrl everywhere
 * else (Windows, Linux, Android/Googlebook, web on non-Apple hosts).
 */
@Immutable
data class KeyShortcut(
    val key: Key,
    val primary: Boolean = false,
    val shift: Boolean = false,
    val alt: Boolean = false,
) {
    fun matches(
        event: KeyEvent,
        metaIsPrimary: Boolean,
    ): Boolean {
        if (event.type != KeyEventType.KeyDown || event.key != key) return false
        val primaryPressed = if (metaIsPrimary) event.isMetaPressed else event.isCtrlPressed
        val otherPrimaryPressed = if (metaIsPrimary) event.isCtrlPressed else event.isMetaPressed
        return primaryPressed == primary &&
            !otherPrimaryPressed &&
            event.isShiftPressed == shift &&
            event.isAltPressed == alt
    }

    /** E.g. "⌘1" on Apple platforms, "Ctrl+1" elsewhere. */
    fun label(metaIsPrimary: Boolean): String =
        buildList {
            if (metaIsPrimary) {
                if (alt) add("⌥")
                if (shift) add("⇧")
                if (primary) add("⌘")
            } else {
                if (primary) add("Ctrl")
                if (alt) add("Alt")
                if (shift) add("Shift")
            }
            add(key.displayName())
        }.joinToString(separator = if (metaIsPrimary) "" else "+")
}

private fun Key.displayName(): String =
    when (this) {
        Key.Escape -> "Esc"
        Key.Comma -> ","
        Key.Period -> "."
        Key.Slash -> "/"
        Key.Enter -> "Enter"
        Key.Spacebar -> "Space"
        Key.Zero -> "0"
        Key.One -> "1"
        Key.Two -> "2"
        Key.Three -> "3"
        Key.Four -> "4"
        Key.Five -> "5"
        Key.Six -> "6"
        Key.Seven -> "7"
        Key.Eight -> "8"
        Key.Nine -> "9"
        else -> toString().removePrefix("Key: ")
    }
