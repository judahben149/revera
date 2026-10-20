package com.jj.revera.shared.action

import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import com.jj.revera.core.navigation.Navigator
import com.jj.revera.core.ui.input.KeyShortcut
import com.jj.revera.feature.home.api.HomeRoute
import com.jj.revera.feature.settings.api.SettingsRoute
import com.jj.revera.shared.resources.Res
import com.jj.revera.shared.resources.action_navigate_back
import com.jj.revera.shared.resources.action_open_home
import com.jj.revera.shared.resources.action_open_settings
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString

/**
 * App-level commands with their keyboard shortcuts, declared once and surfaced everywhere:
 * in-app key handling, the desktop menu bar and Android's Keyboard Shortcuts Helper (Googlebook).
 */
enum class AppAction(
    val shortcut: KeyShortcut,
    internal val labelRes: StringResource,
    val showInMenu: Boolean = true,
) {
    OpenHome(KeyShortcut(Key.One, primary = true), Res.string.action_open_home),
    OpenSettings(KeyShortcut(Key.Comma, primary = true), Res.string.action_open_settings),
    NavigateBack(KeyShortcut(Key.Escape), Res.string.action_navigate_back, showInMenu = false),
}

suspend fun AppAction.label(): String = getString(labelRes)

internal class AppActionDispatcher(
    private val navigator: Navigator,
    private val metaIsPrimary: Boolean,
) {
    /** @return true if the action did something. */
    fun dispatch(action: AppAction): Boolean =
        when (action) {
            AppAction.OpenHome -> {
                navigator.navigateToTopLevel(HomeRoute)
                true
            }

            AppAction.OpenSettings -> {
                navigator.navigateToTopLevel(SettingsRoute)
                true
            }

            AppAction.NavigateBack -> {
                navigator.goBack()
            }
        }

    fun onKeyEvent(event: KeyEvent): Boolean {
        val action = AppAction.entries.firstOrNull { it.shortcut.matches(event, metaIsPrimary) } ?: return false
        return dispatch(action)
    }
}
