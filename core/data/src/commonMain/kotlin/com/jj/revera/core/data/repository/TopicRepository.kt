package com.jj.revera.core.data.repository

import com.jj.revera.core.model.Topic
import kotlinx.coroutines.flow.Flow

interface TopicRepository {
    fun observeTopics(): Flow<List<Topic>>

    fun observeTopic(id: String): Flow<Topic?>
}
