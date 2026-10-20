package com.jj.revera.core.testing

import com.jj.revera.core.data.repository.TopicRepository
import com.jj.revera.core.model.Topic
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

class FakeTopicRepository : TopicRepository {
    private val topics = MutableStateFlow<List<Topic>?>(null)

    /** Emits [value] to collectors. Until the first call, flows stay suspended (a "loading" source). */
    fun send(value: List<Topic>) {
        topics.value = value
    }

    override fun observeTopics(): Flow<List<Topic>> = topics.filterNotNull()

    override fun observeTopic(id: String): Flow<Topic?> = observeTopics().map { all -> all.firstOrNull { it.id == id } }
}

fun testTopic(
    id: String,
    title: String = "Topic $id",
) = Topic(
    id = id,
    title = title,
    summary = "Summary of $title",
    body = "Body of $title",
    readingMinutes = 1,
)
