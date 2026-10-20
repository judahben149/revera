package com.jj.revera.shared

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.v2.runDesktopComposeUiTest
import com.jj.revera.shared.di.reveraModules
import com.russhwolf.settings.MapSettings
import com.russhwolf.settings.Settings
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

/** End-to-end navigation through the real App() at a two-pane (expanded) window size. */
@OptIn(ExperimentalTestApi::class)
class AppNavigationTest {
    @BeforeTest
    fun startDi() {
        startKoin { modules(reveraModules + module { single<Settings> { MapSettings() } }) }
    }

    @AfterTest
    fun stopDi() = stopKoin()

    @Test
    fun listSurvivesSwitchingTabsAndBack() =
        runDesktopComposeUiTest(width = 1200, height = 800) {
            setContent { App() }
            onNodeWithText("Adaptive by default").assertExists()

            onNodeWithText("Settings", useUnmergedTree = true).performClick()
            waitForIdle()
            onNodeWithText("Appearance").assertExists()

            onNodeWithText("Home", useUnmergedTree = true).performClick()
            waitForIdle()
            onNodeWithText("Adaptive by default").assertExists()
        }

    @Test
    fun selectingATopicOpensItBesideTheList() =
        runDesktopComposeUiTest(width = 1200, height = 800) {
            setContent { App() }
            onNodeWithText("Keyboard first").performClick()
            waitForIdle()
            onNodeWithText("Shortcuts are declared once", substring = true).assertExists()
            onNodeWithText("Adaptive by default").assertExists()
        }
}
