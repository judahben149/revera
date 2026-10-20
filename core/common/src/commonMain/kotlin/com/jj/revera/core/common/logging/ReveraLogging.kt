package com.jj.revera.core.common.logging

import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import com.jj.revera.core.common.AppEnvironment

/**
 * Kermit routes to Logcat on Android, OSLog on Apple platforms, stdout on desktop and the
 * browser console on web. Add a crash-reporting writer here when one is chosen.
 */
object ReveraLogging {
    fun initialize(environment: AppEnvironment = AppEnvironment.current) {
        Logger.setTag("Revera")
        Logger.setMinSeverity(if (environment == AppEnvironment.Prod) Severity.Info else Severity.Debug)
    }
}

fun logger(tag: String): Logger = Logger.withTag(tag)
