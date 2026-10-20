package com.jj.revera.shared

import com.jj.revera.shared.action.AppAction
import kotlin.test.Test
import kotlin.test.assertEquals

class AppActionTest {
    @Test
    fun shortcutsAreUnique() {
        val shortcuts = AppAction.entries.map { it.shortcut }
        assertEquals(shortcuts.size, shortcuts.toSet().size, "Two actions share a keyboard shortcut")
    }
}
