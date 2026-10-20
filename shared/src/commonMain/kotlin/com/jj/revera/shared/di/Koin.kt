package com.jj.revera.shared.di

import com.jj.revera.core.common.di.commonModule
import com.jj.revera.core.common.logging.ReveraLogging
import com.jj.revera.core.data.di.dataModule
import com.jj.revera.core.network.di.networkModule
import com.jj.revera.feature.home.impl.di.homeModule
import com.jj.revera.feature.settings.impl.di.settingsModule
import com.jj.revera.shared.AppViewModel
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import org.koin.mp.KoinPlatform

private val appModule =
    module {
        viewModelOf(::AppViewModel)
    }

/** The complete dependency graph. New feature modules add their Koin module here. */
val reveraModules: List<Module> =
    listOf(
        commonModule,
        networkModule,
        dataModule,
        homeModule,
        settingsModule,
        appModule,
    )

/**
 * Starts logging and DI. Call once from each platform entry point before showing [com.jj.revera.shared.App].
 * Safe to call repeatedly (e.g. iPadOS creating several scenes): later calls are no-ops.
 *
 * @param platformDeclaration platform extras, e.g. `androidContext(this)` on Android.
 */
fun initKoin(platformDeclaration: KoinAppDeclaration = {}): KoinApplication? {
    if (KoinPlatform.getKoinOrNull() != null) return null
    ReveraLogging.initialize()
    return startKoin {
        platformDeclaration()
        modules(reveraModules)
    }
}
