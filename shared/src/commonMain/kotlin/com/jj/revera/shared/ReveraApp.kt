package com.jj.revera.shared

import androidx.compose.foundation.focusable
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteItem
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.jj.revera.core.common.platform.PlatformInfo
import com.jj.revera.core.navigation.Navigator
import com.jj.revera.core.navigation.rememberEntries
import com.jj.revera.core.navigation.rememberNavigationState
import com.jj.revera.core.ui.adaptive.LocalReveraWindowInfo
import com.jj.revera.core.ui.adaptive.ReveraWindowInfo
import com.jj.revera.core.ui.adaptive.WindowWidthClass
import com.jj.revera.core.ui.adaptive.currentReveraAdaptiveInfo
import com.jj.revera.core.ui.adaptive.rememberReveraPaneDirective
import com.jj.revera.core.ui.input.handCursor
import com.jj.revera.feature.home.api.HomeRoute
import com.jj.revera.feature.home.impl.navigation.homeEntries
import com.jj.revera.feature.settings.impl.navigation.settingsEntries
import com.jj.revera.shared.action.AppAction
import com.jj.revera.shared.action.AppActionDispatcher
import com.jj.revera.shared.navigation.ReveraNavConfiguration
import com.jj.revera.shared.navigation.TopLevelDestination
import com.jj.revera.shared.navigation.reveraPopTransitionSpec
import com.jj.revera.shared.navigation.reveraTransitionSpec
import kotlinx.coroutines.flow.Flow
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
internal fun ReveraApp(
    actions: Flow<AppAction>,
    modifier: Modifier = Modifier,
) {
    val navigationState =
        rememberNavigationState(
            startRoute = HomeRoute,
            topLevelRoutes = TopLevelDestination.routes,
            configuration = ReveraNavConfiguration,
        )
    val navigator = remember(navigationState) { Navigator(navigationState) }
    val platform = koinInject<PlatformInfo>()
    val dispatcher =
        remember(navigator, platform) { AppActionDispatcher(navigator, platform.usesMetaAsPrimaryModifier) }
    LaunchedEffect(actions, dispatcher) { actions.collect(dispatcher::dispatch) }

    // Keyboard shortcuts need focus inside the tree; take it on launch so they work immediately.
    val rootFocus = remember { FocusRequester() }
    LaunchedEffect(rootFocus) { rootFocus.requestFocus() }

    val adaptiveInfo = currentReveraAdaptiveInfo()
    val suiteType = navigationSuiteType(adaptiveInfo, LocalReveraWindowInfo.current)
    val listDetailStrategy =
        rememberListDetailSceneStrategy<NavKey>(directive = rememberReveraPaneDirective(adaptiveInfo))
    val entryProvider =
        remember(navigator) {
            entryProvider<NavKey> {
                homeEntries(navigator)
                settingsEntries()
            }
        }

    NavigationSuiteScaffold(
        modifier =
            modifier
                .onPreviewKeyEvent(dispatcher::onKeyEvent)
                .focusRequester(rootFocus)
                .focusable(),
        navigationSuiteType = suiteType,
        navigationItems = {
            TopLevelDestination.entries.forEach { destination ->
                val selected = navigationState.topLevelRoute == destination.route
                NavigationSuiteItem(
                    selected = selected,
                    onClick = { navigator.navigateToTopLevel(destination.route) },
                    icon = {
                        Icon(
                            imageVector = if (selected) destination.selectedIcon else destination.unselectedIcon,
                            contentDescription = null,
                        )
                    },
                    label = { Text(stringResource(destination.label)) },
                    navigationSuiteType = suiteType,
                    modifier = Modifier.handCursor(),
                )
            }
        },
    ) {
        NavDisplay(
            entries = navigationState.rememberEntries(entryProvider),
            onBack = { navigator.goBack() },
            sceneStrategies = listOf(listDetailStrategy),
            transitionSpec = reveraTransitionSpec(),
            popTransitionSpec = reveraPopTransitionSpec(),
        )
    }
}

/**
 * Bottom bar on compact windows (and tabletop posture, keeping controls on the lower half),
 * a collapsed rail on medium/expanded, and an expanded rail with labels on large desktop-class
 * windows such as Googlebook, desktop and big tablets.
 */
private fun navigationSuiteType(
    adaptiveInfo: WindowAdaptiveInfo,
    windowInfo: ReveraWindowInfo,
): NavigationSuiteType =
    when (windowInfo.widthClass) {
        WindowWidthClass.Large, WindowWidthClass.ExtraLarge -> NavigationSuiteType.WideNavigationRailExpanded
        else -> NavigationSuiteScaffoldDefaults.navigationSuiteType(adaptiveInfo)
    }
