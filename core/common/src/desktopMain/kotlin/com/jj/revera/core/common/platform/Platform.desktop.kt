package com.jj.revera.core.common.platform

actual fun currentPlatform(): PlatformInfo {
    val osName = System.getProperty("os.name").orEmpty()
    val os =
        when {
            osName.startsWith("Mac", ignoreCase = true) -> HostOs.MacOs
            osName.startsWith("Windows", ignoreCase = true) -> HostOs.Windows
            osName.contains("nux", ignoreCase = true) || osName.contains("nix", ignoreCase = true) -> HostOs.Linux
            else -> HostOs.Unknown
        }
    return PlatformInfo(
        family = PlatformFamily.Desktop,
        os = os,
        description =
            "$osName ${System.getProperty("os.version")} (${System.getProperty("os.arch")}) · " +
                "JVM ${System.getProperty("java.version")}",
    )
}

internal actual fun dynamicColorAvailable(): Boolean = false
