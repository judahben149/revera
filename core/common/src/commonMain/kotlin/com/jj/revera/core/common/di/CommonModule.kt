package com.jj.revera.core.common.di

import com.jj.revera.core.common.dispatcher.ReveraDispatchers
import com.jj.revera.core.common.platform.currentPlatform
import org.koin.dsl.module

val commonModule =
    module {
        single { ReveraDispatchers.platformDefault() }
        single { currentPlatform() }
    }
