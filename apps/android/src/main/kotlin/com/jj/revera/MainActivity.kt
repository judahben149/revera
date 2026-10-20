package com.jj.revera

import android.os.Bundle
import android.view.KeyboardShortcutGroup
import android.view.Menu
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.jj.revera.shared.App
import com.jj.revera.shared.reveraKeyboardShortcutGroup
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private var keyboardShortcuts: KeyboardShortcutGroup? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        // Draw behind system bars and (in desktop windowing) the caption bar; Compose handles insets.
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        lifecycleScope.launch { keyboardShortcuts = reveraKeyboardShortcutGroup() }

        setContent { App() }
    }

    /** Lists Revera's shortcuts in the system Keyboard Shortcuts Helper (Meta + /). */
    override fun onProvideKeyboardShortcuts(
        data: MutableList<KeyboardShortcutGroup>,
        menu: Menu?,
        deviceId: Int,
    ) {
        super.onProvideKeyboardShortcuts(data, menu, deviceId)
        keyboardShortcuts?.let(data::add)
    }
}
