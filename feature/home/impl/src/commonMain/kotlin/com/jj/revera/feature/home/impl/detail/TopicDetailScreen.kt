package com.jj.revera.feature.home.impl.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jj.revera.core.designsystem.components.ReveraBackButton
import com.jj.revera.core.designsystem.components.ReveraEmptyState
import com.jj.revera.core.designsystem.components.ReveraLoadingIndicator
import com.jj.revera.core.designsystem.components.readableWidth
import com.jj.revera.core.designsystem.icons.ReveraIcons
import com.jj.revera.core.designsystem.theme.ReveraTheme
import com.jj.revera.core.model.Topic
import com.jj.revera.core.ui.adaptive.FoldPosture
import com.jj.revera.core.ui.adaptive.LocalReveraWindowInfo
import com.jj.revera.core.ui.adaptive.TabletopLayout
import com.jj.revera.feature.home.impl.resources.Res
import com.jj.revera.feature.home.impl.resources.topic_back
import com.jj.revera.feature.home.impl.resources.topic_not_found
import com.jj.revera.feature.home.impl.resources.topic_placeholder_message
import com.jj.revera.feature.home.impl.resources.topic_placeholder_title
import com.jj.revera.feature.home.impl.resources.topic_reading_time
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
internal fun TopicDetailRoute(
    topicId: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TopicViewModel = koinViewModel { parametersOf(topicId) },
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    TopicDetailScreen(
        uiState = uiState,
        // With list and detail side by side there is nothing to go "back" to visually.
        showBackButton = !LocalReveraWindowInfo.current.showsMultiplePanes,
        onBack = onBack,
        modifier = modifier,
    )
}

@Composable
internal fun TopicDetailScreen(
    uiState: TopicUiState,
    showBackButton: Boolean,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    if (showBackButton) {
                        ReveraBackButton(onClick = onBack, contentDescription = stringResource(Res.string.topic_back))
                    }
                },
            )
        },
    ) { padding ->
        val contentModifier = Modifier.fillMaxSize().padding(padding)
        when (uiState) {
            TopicUiState.Loading -> {
                ReveraLoadingIndicator(contentModifier)
            }

            TopicUiState.NotFound -> {
                ReveraEmptyState(
                    icon = ReveraIcons.Info,
                    title = stringResource(Res.string.topic_not_found),
                    modifier = contentModifier,
                )
            }

            is TopicUiState.Success -> {
                TopicContent(uiState.topic, contentModifier)
            }
        }
    }
}

@Composable
private fun TopicContent(
    topic: Topic,
    modifier: Modifier = Modifier,
) {
    val posture = LocalReveraWindowInfo.current.posture
    if (posture is FoldPosture.Tabletop) {
        // Half-folded like a laptop: headline above the hinge, scrollable body below it.
        TabletopLayout(
            posture = posture,
            modifier = modifier,
            top = {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    TopicHeader(topic, Modifier.readableWidth().padding(ReveraTheme.spacing.xl))
                }
            },
            bottom = {
                TopicBody(
                    topic,
                    Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .readableWidth()
                        .padding(ReveraTheme.spacing.xl),
                )
            },
        )
    } else {
        Column(
            modifier =
                modifier
                    .verticalScroll(rememberScrollState())
                    .readableWidth()
                    .padding(horizontal = ReveraTheme.spacing.xl, vertical = ReveraTheme.spacing.lg),
            verticalArrangement = Arrangement.spacedBy(ReveraTheme.spacing.xl),
        ) {
            TopicHeader(topic)
            TopicBody(topic)
        }
    }
}

@Composable
private fun TopicHeader(
    topic: Topic,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(ReveraTheme.spacing.sm)) {
        Text(
            text = stringResource(Res.string.topic_reading_time, topic.readingMinutes),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(text = topic.title, style = MaterialTheme.typography.displaySmall)
        Text(
            text = topic.summary,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun TopicBody(
    topic: Topic,
    modifier: Modifier = Modifier,
) {
    Text(text = topic.body, style = MaterialTheme.typography.bodyLarge, modifier = modifier)
}

/** Shown in the detail pane of wide layouts before anything is selected. */
@Composable
internal fun TopicDetailPlaceholder(modifier: Modifier = Modifier) {
    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colorScheme.surfaceContainerLowest) {
        ReveraEmptyState(
            icon = ReveraIcons.Article,
            title = stringResource(Res.string.topic_placeholder_title),
            message = stringResource(Res.string.topic_placeholder_message),
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
