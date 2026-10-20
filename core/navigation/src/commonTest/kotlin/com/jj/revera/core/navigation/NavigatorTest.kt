package com.jj.revera.core.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class NavigatorTest {
    private data object Home : NavKey

    private data object Settings : NavKey

    private data class Detail(
        val id: String,
    ) : NavKey

    private fun navigator(): Navigator {
        val state =
            NavigationState(
                startRoute = Home,
                topLevelStack = NavBackStack(Home),
                subStacks = mapOf(Home to NavBackStack(Home), Settings to NavBackStack(Settings)),
            )
        return Navigator(state)
    }

    @Test
    fun pushesOntoCurrentTab() {
        val navigator = navigator()
        navigator.navigate(Detail("a"))
        assertEquals(listOf(Home, Detail("a")), navigator.state.currentStack)
    }

    @Test
    fun replaceWherePopsMatchingEntriesFirst() {
        val navigator = navigator()
        navigator.navigate(Detail("a"))
        navigator.navigate(Detail("b")) { it is Detail }
        assertEquals(listOf(Home, Detail("b")), navigator.state.currentStack)
    }

    @Test
    fun tabsKeepTheirOwnHistory() {
        val navigator = navigator()
        navigator.navigate(Detail("a"))
        navigator.navigate(Settings)
        assertEquals(Settings, navigator.state.currentRoute)

        navigator.navigate(Home)
        assertEquals(listOf(Home, Detail("a")), navigator.state.currentStack)
    }

    @Test
    fun reselectingCurrentTabPopsToRoot() {
        val navigator = navigator()
        navigator.navigate(Detail("a"))
        navigator.navigateToTopLevel(Home)
        assertEquals(listOf(Home), navigator.state.currentStack)
    }

    @Test
    fun backReturnsToStartTabThenReportsNothingLeft() {
        val navigator = navigator()
        navigator.navigate(Settings)

        assertTrue(navigator.goBack())
        assertEquals(Home, navigator.state.topLevelRoute)
        assertFalse(navigator.goBack())
    }

    @Test
    fun stacksInUseKeepStartUnderneath() {
        val navigator = navigator()
        assertEquals(listOf<NavKey>(Home), navigator.state.stacksInUse)
        navigator.navigate(Settings)
        assertEquals(listOf(Home, Settings), navigator.state.stacksInUse)
    }
}
