package com.jj.revera.shared

import androidx.compose.ui.window.ComposeUIViewController
import com.jj.revera.shared.di.initKoin
import platform.UIKit.UIViewController

/** Swift entry point: `MainViewControllerKt.MainViewController()` (see apps/ios). */
@Suppress("ktlint:standard:function-naming", "FunctionName")
fun MainViewController(): UIViewController {
    initKoin()
    return ComposeUIViewController { App() }
}
