package com.jj.revera.feature.home.impl.di

import com.jj.revera.feature.home.impl.detail.TopicViewModel
import com.jj.revera.feature.home.impl.list.HomeViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val homeModule =
    module {
        viewModelOf(::HomeViewModel)
        viewModelOf(::TopicViewModel)
    }
