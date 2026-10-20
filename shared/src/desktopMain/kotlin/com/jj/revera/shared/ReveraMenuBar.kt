package com.jj.revera.shared

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.input.key.KeyShortcut
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.MenuBar
import com.jj.revera.core.common.platform.currentPlatform
import com.jj.revera.shared.action.AppAction
import com.jj.revera.shared.resources.Res
import com.jj.revera.shared.resources.menu_go
import org.jetbrains.compose.resources.stringResource
import com.jj.revera.core.ui.input.KeyShortcut as ReveraKeyShortcut

/** Native menu bar (the macOS screen menu bar; an in-window menu on Windows/Linux). */
@Composable
fun FrameWindowScope.ReveraMenuBar(onAction: (AppAction) -> Unit) {
    val metaIsPrimary = remember { currentPlatform().usesMetaAsPrimaryModifier }
    MenuBar {
        Menu(stringResource(Res.string.menu_go)) {
            AppAction.entries.filter(AppAction::showInMenu).forEach { action ->
                Item(
                    text = stringResource(action.labelRes),
                    shortcut = action.shortcut.toAwtShortcut(metaIsPrimary),
                    onClick = { onAction(action) },
                )
            }
        }
    }
}

private fun ReveraKeyShortcut.toAwtShortcut(metaIsPrimary: Boolean) =
    KeyShortcut(
        key = key,
        ctrl = primary && !metaIsPrimary,
        meta = primary && metaIsPrimary,
        alt = alt,
        shift = shift,
    )
