package com.jj.revera.shared

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jj.revera.core.designsystem.theme.ReveraTheme
import com.jj.revera.core.model.ThemeMode
import com.jj.revera.core.ui.adaptive.ProvideReveraWindowInfo
import com.jj.revera.shared.action.AppAction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import org.koin.compose.viewmodel.koinViewModel

/**
 * The whole Revera UI. Every platform shell (Android, iOS, desktop, web) renders exactly this.
 *
 * @param actions app-level commands from outside Compose, e.g. the desktop menu bar.
 */
@Composable
fun App(
    modifier: Modifier = Modifier,
    actions: Flow<AppAction> = emptyFlow(),
) {
    val viewModel: AppViewModel = koinViewModel()
    val preferences by viewModel.preferences.collectAsStateWithLifecycle()
    val darkTheme =
        when (preferences.themeMode) {
            ThemeMode.System -> isSystemInDarkTheme()
            ThemeMode.Light -> false
            ThemeMode.Dark -> true
        }

    ReveraTheme(darkTheme = darkTheme, dynamicColor = preferences.useDynamicColor) {
        ProvideReveraWindowInfo {
            ReveraApp(actions = actions, modifier = modifier)
        }
    }
}
