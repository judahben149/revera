package com.jj.revera.shared

import com.jj.revera.core.common.platform.HostOs
import com.jj.revera.core.common.platform.PlatformFamily
import com.jj.revera.shared.di.reveraModules
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.module
import org.koin.test.verify.verify
import kotlin.test.Test

class DependencyGraphTest {
    /** Fails if any definition in the app graph has a dependency nobody provides. */
    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun appGraphIsComplete() {
        module { includes(reveraModules) }.verify(
            extraTypes =
                listOf(
                    // Runtime parameters supplied via parametersOf(...), e.g. TopicViewModel's topicId.
                    String::class,
                    // Built by factory functions (ReveraDispatchers.platformDefault(), currentPlatform()),
                    // whose constructor parameters verify() can't see through.
                    CoroutineDispatcher::class,
                    PlatformFamily::class,
                    HostOs::class,
                    // Ktor's HttpClient(engine, config) — the engine is resolved by Ktor, not Koin.
                    HttpClientEngine::class,
                    HttpClientConfig::class,
                ),
        )
    }
}
