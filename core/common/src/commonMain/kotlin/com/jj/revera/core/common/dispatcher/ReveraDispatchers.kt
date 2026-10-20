package com.jj.revera.core.common.dispatcher

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Injected instead of referencing [Dispatchers] directly, so tests can swap in a
 * `StandardTestDispatcher` and platforms without `Dispatchers.IO` (web) still work.
 */
data class ReveraDispatchers(
    val main: CoroutineDispatcher,
    val default: CoroutineDispatcher,
    val io: CoroutineDispatcher,
) {
    companion object {
        fun platformDefault(): ReveraDispatchers =
            ReveraDispatchers(
                main = Dispatchers.Main,
                default = Dispatchers.Default,
                io = ioDispatcher,
            )
    }
}

internal expect val ioDispatcher: CoroutineDispatcher
