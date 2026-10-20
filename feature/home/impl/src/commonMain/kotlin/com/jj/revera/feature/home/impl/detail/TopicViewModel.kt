package com.jj.revera.feature.home.impl.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jj.revera.core.data.repository.TopicRepository
import com.jj.revera.core.model.Topic
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

internal class TopicViewModel(
    topicId: String,
    topicRepository: TopicRepository,
) : ViewModel() {
    val uiState: StateFlow<TopicUiState> =
        topicRepository
            .observeTopic(topicId)
            .map { topic -> if (topic == null) TopicUiState.NotFound else TopicUiState.Success(topic) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), TopicUiState.Loading)
}

internal sealed interface TopicUiState {
    data object Loading : TopicUiState

    data object NotFound : TopicUiState

    data class Success(
        val topic: Topic,
    ) : TopicUiState
}
