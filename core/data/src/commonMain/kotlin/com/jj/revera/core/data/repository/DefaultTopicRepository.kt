package com.jj.revera.core.data.repository

import com.jj.revera.core.data.sample.SampleTopics
import com.jj.revera.core.model.Topic
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

/**
 * Serves bundled sample content. Swap the backing source for a network/database-backed one
 * (offline-first: database as source of truth, network sync writes into it) without touching
 * any feature code — features only see [TopicRepository].
 */
internal class DefaultTopicRepository : TopicRepository {
    private val topics = MutableStateFlow(SampleTopics.all)

    override fun observeTopics(): Flow<List<Topic>> = topics.asStateFlow()

    override fun observeTopic(id: String): Flow<Topic?> = topics.map { all -> all.firstOrNull { it.id == id } }
}
