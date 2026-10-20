package com.jj.revera.core.common.platform

import android.os.Build

actual fun currentPlatform(): PlatformInfo =
    PlatformInfo(
        family = PlatformFamily.Android,
        os = HostOs.Android,
        description =
            "Android ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT}) · " +
                "${Build.MANUFACTURER.replaceFirstChar(Char::uppercase)} ${Build.MODEL}",
    )

internal actual fun dynamicColorAvailable(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
