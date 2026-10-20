package com.jj.revera.core.common.platform

@OptIn(ExperimentalWasmJsInterop::class)
private fun userAgent(): String = js("navigator.userAgent")

actual fun currentPlatform(): PlatformInfo {
    val userAgent = userAgent()
    val os =
        when {
            userAgent.contains("iPhone") || userAgent.contains("iPod") -> HostOs.Ios
            userAgent.contains("iPad") -> HostOs.IpadOs
            userAgent.contains("Mac OS X") || userAgent.contains("Macintosh") -> HostOs.MacOs
            userAgent.contains("Android") -> HostOs.Android
            userAgent.contains("Windows") -> HostOs.Windows
            userAgent.contains("Linux") || userAgent.contains("CrOS") -> HostOs.Linux
            else -> HostOs.Unknown
        }
    return PlatformInfo(
        family = PlatformFamily.Web,
        os = os,
        description = "Web (Kotlin/Wasm) · $userAgent",
    )
}

internal actual fun dynamicColorAvailable(): Boolean = false
