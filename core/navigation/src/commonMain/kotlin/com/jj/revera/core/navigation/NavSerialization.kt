package com.jj.revera.core.navigation

import androidx.navigation3.runtime.NavKey
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.modules.PolymorphicModuleBuilder
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

/**
 * Registers a feature's routes for saving. Each feature `api` module exposes one, e.g.
 * `val homeRoutes: NavKeyRegistration = { subclass(HomeRoute::class, HomeRoute.serializer()) }`.
 */
typealias NavKeyRegistration = PolymorphicModuleBuilder<NavKey>.() -> Unit

/**
 * Non-JVM platforms have no reflection, so every saveable [NavKey] must be registered
 * explicitly. Build this once from all features' registrations.
 */
fun navSavedStateConfiguration(vararg registrations: NavKeyRegistration): SavedStateConfiguration =
    SavedStateConfiguration {
        serializersModule =
            SerializersModule {
                polymorphic(NavKey::class) {
                    registrations.forEach { register -> register() }
                }
            }
    }
