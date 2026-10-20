package com.jj.revera.feature.home.impl.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jj.revera.core.data.repository.TopicRepository
import com.jj.revera.core.model.Topic
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

internal class HomeViewModel(
    topicRepository: TopicRepository,
) : ViewModel() {
    val uiState: StateFlow<HomeUiState> =
        topicRepository
            .observeTopics()
            .map<List<Topic>, HomeUiState> { HomeUiState.Success(it.toImmutableList()) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), HomeUiState.Loading)
}

internal sealed interface HomeUiState {
    data object Loading : HomeUiState

    data class Success(
        val topics: ImmutableList<Topic>,
    ) : HomeUiState
}
