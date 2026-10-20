package com.jj.revera.feature.home.impl

import app.cash.turbine.test
import com.jj.revera.core.testing.FakeTopicRepository
import com.jj.revera.core.testing.MainDispatcherTest
import com.jj.revera.core.testing.testTopic
import com.jj.revera.feature.home.impl.detail.TopicUiState
import com.jj.revera.feature.home.impl.detail.TopicViewModel
import com.jj.revera.feature.home.impl.list.HomeUiState
import com.jj.revera.feature.home.impl.list.HomeViewModel
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class HomeViewModelTest : MainDispatcherTest() {
    private val repository = FakeTopicRepository()

    @Test
    fun startsLoadingThenShowsTopics() =
        runTest {
            val viewModel = HomeViewModel(repository)
            viewModel.uiState.test {
                assertEquals(HomeUiState.Loading, awaitItem())
                repository.send(listOf(testTopic("a"), testTopic("b")))
                val success = awaitItem() as HomeUiState.Success
                assertEquals(listOf("a", "b"), success.topics.map { it.id })
            }
        }

    @Test
    fun topicViewModelReportsNotFoundForUnknownIds() =
        runTest {
            val viewModel = TopicViewModel("missing", repository)
            viewModel.uiState.test {
                assertEquals(TopicUiState.Loading, awaitItem())
                repository.send(listOf(testTopic("a")))
                assertEquals(TopicUiState.NotFound, awaitItem())
            }
        }
}
