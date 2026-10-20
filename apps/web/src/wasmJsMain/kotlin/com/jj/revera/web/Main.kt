package com.jj.revera.web

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.jj.revera.shared.App
import com.jj.revera.shared.di.initKoin

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    initKoin()
    ComposeViewport(viewportContainerId = "revera") {
        App()
    }
    removeSplash()
}

@OptIn(ExperimentalWasmJsInterop::class)
private fun removeSplash(): Unit = js("document.getElementById('splash')?.remove()")
