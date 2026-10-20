package com.jj.revera.core.common

import kotlin.test.Test
import kotlin.test.assertTrue

class AppEnvironmentTest {
    @Test
    fun buildConfigEnvironmentMapsToAKnownEnvironment() {
        assertTrue(
            AppEnvironment.entries.any { it.name.equals(BuildConfig.ENVIRONMENT, ignoreCase = true) },
            "revera.environment='${BuildConfig.ENVIRONMENT}' is not one of ${AppEnvironment.entries}",
        )
    }

    @Test
    fun versionNameIsSemantic() {
        assertTrue(Regex("""\d+\.\d+\.\d+.*""").matches(BuildConfig.VERSION_NAME))
    }
}
