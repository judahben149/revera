package com.jj.revera.core.model

/** A piece of content shown in the Home list/detail experience. */
data class Topic(
    val id: String,
    val title: String,
    val summary: String,
    val body: String,
    val readingMinutes: Int,
)
