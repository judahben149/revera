package com.jj.revera.core.navigation

import androidx.compose.runtime.Stable
import androidx.navigation3.runtime.NavKey

/**
 * The only API features use to move around. Features receive a [Navigator] and call it with
 * routes from other features' `api` modules — they never touch back stacks directly.
 */
@Stable
class Navigator(
    val state: NavigationState,
) {
    /**
     * Pushes [route] onto the current tab, or switches tab when [route] is top-level.
     *
     * @param replaceWhere pops trailing entries matching this predicate first — e.g. so picking
     *   a different item in a list/detail layout replaces the open detail instead of stacking it.
     */
    fun navigate(
        route: NavKey,
        replaceWhere: (NavKey) -> Boolean = { false },
    ) {
        if (route in state.topLevelRoutes) {
            navigateToTopLevel(route)
            return
        }
        val stack = state.currentSubStack
        while (stack.size > 1 && replaceWhere(stack.last())) stack.removeAt(stack.lastIndex)
        if (stack.last() != route) stack.add(route)
    }

    /** Switches tab; re-selecting the current tab pops it back to its root. */
    fun navigateToTopLevel(route: NavKey) {
        if (state.topLevelRoute == route) {
            val stack = state.currentSubStack
            while (stack.size > 1) stack.removeAt(stack.lastIndex)
        } else {
            state.selectTopLevel(route)
        }
    }

    /** @return false when there is nothing left to pop (let the platform handle Back, e.g. exit). */
    fun goBack(): Boolean {
        val stack = state.currentSubStack
        return when {
            stack.size > 1 -> {
                stack.removeAt(stack.lastIndex)
                true
            }

            state.topLevelRoute != state.startRoute -> {
                state.selectTopLevel(state.startRoute)
                true
            }

            else -> {
                false
            }
        }
    }
}
