package com.jj.revera.core.ui.input

import androidx.compose.ui.input.key.Key
import kotlin.test.Test
import kotlin.test.assertEquals

class KeyShortcutTest {
    @Test
    fun labelsFollowPlatformConventions() {
        val shortcut = KeyShortcut(Key.One, primary = true)
        assertEquals("⌘1", shortcut.label(metaIsPrimary = true))
        assertEquals("Ctrl+1", shortcut.label(metaIsPrimary = false))
    }

    @Test
    fun modifierOnlyShortcutLabels() {
        assertEquals("Esc", KeyShortcut(Key.Escape).label(metaIsPrimary = false))
        assertEquals("⇧⌘,", KeyShortcut(Key.Comma, primary = true, shift = true).label(metaIsPrimary = true))
        assertEquals("Ctrl+Shift+,", KeyShortcut(Key.Comma, primary = true, shift = true).label(metaIsPrimary = false))
    }
}
