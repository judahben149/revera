package com.jj.revera.feature.home.api

import androidx.navigation3.runtime.NavKey
import com.jj.revera.core.navigation.NavKeyRegistration
import kotlinx.serialization.Serializable

/** Top-level Home destination: the topic list. */
@Serializable
data object HomeRoute : NavKey

/** A single topic. On wide windows it opens beside the list instead of replacing it. */
@Serializable
data class TopicRoute(
    val topicId: String,
) : NavKey

val homeRoutes: NavKeyRegistration = {
    subclass(HomeRoute::class, HomeRoute.serializer())
    subclass(TopicRoute::class, TopicRoute.serializer())
}
