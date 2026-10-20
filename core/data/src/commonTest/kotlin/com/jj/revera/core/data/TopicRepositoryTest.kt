package com.jj.revera.core.data

import com.jj.revera.core.data.repository.DefaultTopicRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class TopicRepositoryTest {
    private val repository = DefaultTopicRepository()

    @Test
    fun topicIdsAreUnique() =
        runTest {
            val ids = repository.observeTopics().first().map { it.id }
            assertTrue(ids.isNotEmpty())
            assertEquals(ids.size, ids.toSet().size)
        }

    @Test
    fun observeTopicResolvesByIdAndReturnsNullForUnknownIds() =
        runTest {
            val first = repository.observeTopics().first().first()
            assertEquals(first, repository.observeTopic(first.id).first())
            assertNull(repository.observeTopic("does-not-exist").first())
        }
}
