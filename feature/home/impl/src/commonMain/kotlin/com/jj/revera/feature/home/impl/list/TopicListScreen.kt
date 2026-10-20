package com.jj.revera.feature.home.impl.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jj.revera.core.designsystem.components.ReveraEmptyState
import com.jj.revera.core.designsystem.components.ReveraLoadingIndicator
import com.jj.revera.core.designsystem.icons.ReveraIcons
import com.jj.revera.core.designsystem.theme.ReveraTheme
import com.jj.revera.core.model.Topic
import com.jj.revera.core.ui.input.handCursor
import com.jj.revera.feature.home.impl.resources.Res
import com.jj.revera.feature.home.impl.resources.home_empty_title
import com.jj.revera.feature.home.impl.resources.home_title
import com.jj.revera.feature.home.impl.resources.topic_reading_time
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

/** Stateful entry point: wires the ViewModel. Kept separate so [TopicListScreen] is previewable/testable. */
@Composable
internal fun TopicListRoute(
    selectedTopicId: String?,
    onTopicClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    TopicListScreen(
        uiState = uiState,
        selectedTopicId = selectedTopicId,
        onTopicClick = onTopicClick,
        modifier = modifier,
    )
}

@Composable
internal fun TopicListScreen(
    uiState: HomeUiState,
    selectedTopicId: String?,
    onTopicClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = { TopAppBar(title = { Text(stringResource(Res.string.home_title)) }) },
    ) { padding ->
        when (uiState) {
            HomeUiState.Loading -> {
                ReveraLoadingIndicator(Modifier.fillMaxSize().padding(padding))
            }

            is HomeUiState.Success -> {
                if (uiState.topics.isEmpty()) {
                    ReveraEmptyState(
                        icon = ReveraIcons.Article,
                        title = stringResource(Res.string.home_empty_title),
                        modifier = Modifier.fillMaxSize().padding(padding),
                    )
                } else {
                    TopicList(
                        topics = uiState.topics,
                        selectedTopicId = selectedTopicId,
                        onTopicClick = onTopicClick,
                        contentPadding = padding,
                    )
                }
            }
        }
    }
}

@Composable
private fun TopicList(
    topics: List<Topic>,
    selectedTopicId: String?,
    onTopicClick: (String) -> Unit,
    contentPadding: PaddingValues,
) {
    val spacing = ReveraTheme.spacing
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(contentPadding),
        contentPadding = PaddingValues(horizontal = spacing.md, vertical = spacing.sm),
        verticalArrangement = Arrangement.spacedBy(spacing.xs),
    ) {
        items(topics, key = Topic::id) { topic ->
            TopicListItem(
                topic = topic,
                selected = topic.id == selectedTopicId,
                onClick = { onTopicClick(topic.id) },
            )
        }
    }
}

@Composable
private fun TopicListItem(
    topic: Topic,
    selected: Boolean,
    onClick: () -> Unit,
) {
    ListItem(
        selected = selected,
        onClick = onClick,
        modifier = Modifier.handCursor(),
        colors =
            ListItemDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
        shapes =
            ListItemDefaults.shapes(
                shape = MaterialTheme.shapes.medium,
                selectedShape = MaterialTheme.shapes.large,
            ),
        leadingContent = { Icon(imageVector = ReveraIcons.Article, contentDescription = null) },
        supportingContent = {
            Text(
                text = topic.summary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        },
        trailingContent = {
            Text(
                text = stringResource(Res.string.topic_reading_time, topic.readingMinutes),
                style = MaterialTheme.typography.labelMedium,
            )
        },
    ) {
        Text(topic.title, style = MaterialTheme.typography.titleMedium)
    }
}
