package com.jj.revera.shared

import android.view.KeyEvent
import android.view.KeyboardShortcutGroup
import android.view.KeyboardShortcutInfo
import androidx.compose.ui.input.key.nativeKeyCode
import com.jj.revera.core.ui.input.KeyShortcut
import com.jj.revera.shared.action.AppAction
import com.jj.revera.shared.action.label
import com.jj.revera.shared.resources.Res
import com.jj.revera.shared.resources.app_name
import org.jetbrains.compose.resources.getString

/**
 * Revera's shortcuts for the system Keyboard Shortcuts Helper (Meta + / on Googlebook, ChromeOS
 * and Android tablets with keyboards). Resolve once, then return from
 * `Activity.onProvideKeyboardShortcuts`.
 */
suspend fun reveraKeyboardShortcutGroup(): KeyboardShortcutGroup =
    KeyboardShortcutGroup(
        getString(Res.string.app_name),
        AppAction.entries.map { action ->
            KeyboardShortcutInfo(action.label(), action.shortcut.key.nativeKeyCode, action.shortcut.androidMetaState())
        },
    )

private fun KeyShortcut.androidMetaState(): Int {
    var meta = 0
    if (primary) meta = meta or KeyEvent.META_CTRL_ON
    if (shift) meta = meta or KeyEvent.META_SHIFT_ON
    if (alt) meta = meta or KeyEvent.META_ALT_ON
    return meta
}
