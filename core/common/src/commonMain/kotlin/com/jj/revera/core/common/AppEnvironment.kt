package com.jj.revera.core.common

/** Which backend/config tier this build targets. Set via `-Prevera.environment=<name>`. */
enum class AppEnvironment {
    Dev,
    Staging,
    Prod,
    ;

    companion object {
        val current: AppEnvironment by lazy {
            entries.firstOrNull { it.name.equals(BuildConfig.ENVIRONMENT, ignoreCase = true) } ?: Dev
        }
    }
}
