package com.jj.revera.desktop

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.jj.revera.desktop.resources.Res
import com.jj.revera.desktop.resources.revera_icon
import com.jj.revera.shared.App
import com.jj.revera.shared.ReveraMenuBar
import com.jj.revera.shared.action.AppAction
import com.jj.revera.shared.di.initKoin
import kotlinx.coroutines.flow.MutableSharedFlow
import org.jetbrains.compose.resources.painterResource
import java.awt.Dimension

fun main() {
    configurePlatformLookAndFeel()
    initKoin()

    application {
        val windowState =
            rememberWindowState(
                size = DpSize(1280.dp, 832.dp),
                position = WindowPosition(Alignment.Center),
            )
        val actions = remember { MutableSharedFlow<AppAction>(extraBufferCapacity = 8) }

        Window(
            onCloseRequest = ::exitApplication,
            state = windowState,
            title = "Revera",
            icon = painterResource(Res.drawable.revera_icon),
        ) {
            // Narrow enough to exercise the compact (phone) layout when resized.
            LaunchedEffect(window) { window.minimumSize = Dimension(360, 480) }
            ReveraMenuBar(onAction = { actions.tryEmit(it) })
            App(actions = actions)
        }
    }
}

/** Must run before any AWT class loads. No-ops on Windows/Linux. */
private fun configurePlatformLookAndFeel() {
    // Title bar follows the system light/dark appearance; menus live in the macOS screen menu bar.
    System.setProperty("apple.awt.application.appearance", "system")
    System.setProperty("apple.awt.application.name", "Revera")
    System.setProperty("apple.laf.useScreenMenuBar", "true")
}
