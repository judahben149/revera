package com.jj.revera.core.data.di

import com.jj.revera.core.data.repository.DefaultTopicRepository
import com.jj.revera.core.data.repository.DefaultUserPreferencesRepository
import com.jj.revera.core.data.repository.TopicRepository
import com.jj.revera.core.data.repository.UserPreferencesRepository
import com.jj.revera.core.preferences.di.preferencesModule
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dataModule =
    module {
        includes(preferencesModule)
        singleOf(::DefaultTopicRepository) { bind<TopicRepository>() }
        singleOf(::DefaultUserPreferencesRepository) { bind<UserPreferencesRepository>() }
    }
