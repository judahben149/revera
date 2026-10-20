package com.jj.revera.feature.home.impl

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.v2.runComposeUiTest
import com.jj.revera.core.designsystem.theme.ReveraTheme
import com.jj.revera.core.testing.testTopic
import com.jj.revera.feature.home.impl.list.HomeUiState
import com.jj.revera.feature.home.impl.list.TopicListScreen
import kotlinx.collections.immutable.persistentListOf
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class TopicListScreenTest {
    @Test
    fun clickingATopicReportsItsIdAndSelectionIsShown() =
        runComposeUiTest {
            var clicked: String? = null
            setContent {
                ReveraTheme {
                    TopicListScreen(
                        uiState =
                            HomeUiState.Success(
                                persistentListOf(testTopic("a", "Alpha"), testTopic("b", "Beta")),
                            ),
                        selectedTopicId = "a",
                        onTopicClick = { clicked = it },
                    )
                }
            }

            onNodeWithText("Alpha").assertIsSelected()
            onNodeWithText("Beta").performClick()
            assertEquals("b", clicked)
        }
}
