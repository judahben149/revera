package com.jj.revera.feature.home.impl.navigation

import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.jj.revera.core.navigation.Navigator
import com.jj.revera.feature.home.api.HomeRoute
import com.jj.revera.feature.home.api.TopicRoute
import com.jj.revera.feature.home.impl.detail.TopicDetailPlaceholder
import com.jj.revera.feature.home.impl.detail.TopicDetailRoute
import com.jj.revera.feature.home.impl.list.TopicListRoute

/**
 * Registers Home's screens. The list/detail pane metadata lets the app's ListDetailSceneStrategy
 * show them side by side when the window is wide enough (and keep them off the hinge on foldables),
 * or one at a time on compact windows.
 */
fun EntryProviderScope<NavKey>.homeEntries(navigator: Navigator) {
    entry<HomeRoute>(
        metadata = ListDetailSceneStrategy.listPane(detailPlaceholder = { TopicDetailPlaceholder() }),
    ) {
        TopicListRoute(
            selectedTopicId = (navigator.state.currentRoute as? TopicRoute)?.topicId,
            onTopicClick = { topicId ->
                navigator.navigate(TopicRoute(topicId), replaceWhere = { it is TopicRoute })
            },
        )
    }
    entry<TopicRoute>(metadata = ListDetailSceneStrategy.detailPane()) { route ->
        TopicDetailRoute(topicId = route.topicId, onBack = { navigator.goBack() })
    }
}
