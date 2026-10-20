package com.jj.revera.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.structuralEqualityPolicy
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.savedstate.serialization.SavedStateConfiguration

/**
 * Navigation state with one back stack per top-level destination, so every tab keeps its own
 * history (switch to Settings and back, and Home is exactly where you left it).
 *
 * All stacks are saved with kotlinx.serialization, so they survive configuration changes and
 * process death on Android and are restorable on every other platform.
 */
@Stable
class NavigationState internal constructor(
    val startRoute: NavKey,
    private val topLevelStack: NavBackStack<NavKey>,
    internal val subStacks: Map<NavKey, NavBackStack<NavKey>>,
) {
    val topLevelRoute: NavKey
        get() = topLevelStack.last()

    val topLevelRoutes: Set<NavKey>
        get() = subStacks.keys

    internal val currentSubStack: NavBackStack<NavKey>
        get() = subStacks.getValue(topLevelRoute)

    /** The route on top of the visible stack. */
    val currentRoute: NavKey
        get() = currentSubStack.last()

    /** A read-only snapshot of the visible stack, bottom to top. Mutate it only through [Navigator]. */
    val currentStack: List<NavKey>
        get() = currentSubStack.toList()

    /**
     * Stacks rendered by NavDisplay, bottom to top. The start destination always sits underneath,
     * so Back from the root of any other tab returns to it.
     */
    internal val stacksInUse: List<NavKey>
        get() = if (topLevelRoute == startRoute) listOf(startRoute) else listOf(startRoute, topLevelRoute)

    internal fun selectTopLevel(route: NavKey) {
        require(route in subStacks) { "$route is not a top-level route" }
        topLevelStack[0] = route
    }
}

/**
 * @param configuration serializers for every [NavKey] in the app; see [navSavedStateConfiguration].
 */
@Composable
fun rememberNavigationState(
    startRoute: NavKey,
    topLevelRoutes: Set<NavKey>,
    configuration: SavedStateConfiguration,
): NavigationState {
    require(startRoute in topLevelRoutes) { "startRoute must be one of topLevelRoutes" }
    val topLevelStack = rememberNavBackStack(configuration, startRoute)
    val subStacks = topLevelRoutes.associateWith { route -> rememberNavBackStack(configuration, route) }
    return remember(startRoute, topLevelRoutes, configuration) {
        NavigationState(startRoute = startRoute, topLevelStack = topLevelStack, subStacks = subStacks)
    }
}

/**
 * Decorated entries for every stack in use, flattened for `NavDisplay(entries = ...)`.
 * Each stack is decorated separately so per-entry saveable state and ViewModels survive while
 * a tab is in the background.
 */
@Composable
fun NavigationState.rememberEntries(entryProvider: (NavKey) -> NavEntry<NavKey>): List<NavEntry<NavKey>> {
    val decoratedByStack =
        subStacks.mapValues { (_, stack) ->
            rememberDecoratedNavEntries(
                backStack = stack,
                entryDecorators =
                    listOf(
                        rememberSaveableStateHolderNavEntryDecorator(),
                        rememberViewModelStoreNavEntryDecorator(),
                    ),
                entryProvider = entryProvider,
            )
        }
    val currentDecorated = rememberUpdatedState(decoratedByStack)
    // NavDisplay keys pop detection and entry decoration on this list, so it must stay the same
    // instance until its contents actually change (structural equality, not a new list per frame).
    val entries by remember(this) {
        derivedStateOf(structuralEqualityPolicy()) {
            stacksInUse.flatMap { currentDecorated.value.getValue(it) }
        }
    }
    return entries
}
