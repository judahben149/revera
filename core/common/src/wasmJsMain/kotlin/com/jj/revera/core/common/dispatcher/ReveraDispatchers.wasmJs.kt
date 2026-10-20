package com.jj.revera.core.common.dispatcher

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

// The browser is single-threaded; there is no dedicated IO pool.
internal actual val ioDispatcher: CoroutineDispatcher = Dispatchers.Default
