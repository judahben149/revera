package com.jj.revera.core.common.platform

import platform.UIKit.UIDevice
import platform.UIKit.UIUserInterfaceIdiomPad

actual fun currentPlatform(): PlatformInfo {
    val device = UIDevice.currentDevice
    val isPad = device.userInterfaceIdiom == UIUserInterfaceIdiomPad
    return PlatformInfo(
        family = PlatformFamily.Ios,
        os = if (isPad) HostOs.IpadOs else HostOs.Ios,
        description = "${if (isPad) "iPadOS" else "iOS"} ${device.systemVersion} · ${device.model}",
    )
}

internal actual fun dynamicColorAvailable(): Boolean = false
