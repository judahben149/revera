package com.jj.revera.core.network.di

import com.jj.revera.core.network.ReveraJson
import com.jj.revera.core.network.createHttpClient
import org.koin.dsl.module

val networkModule =
    module {
        single { ReveraJson }
        single { createHttpClient(json = get()) }
    }
