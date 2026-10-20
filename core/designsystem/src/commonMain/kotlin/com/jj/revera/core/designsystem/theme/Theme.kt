package com.jj.revera.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import com.materialkolor.PaletteStyle
import com.materialkolor.rememberDynamicColorScheme

/**
 * Root theme for every Revera surface.
 *
 * @param dynamicColor use the platform's wallpaper-derived scheme when available (Android 12+);
 *   falls back to the brand scheme everywhere else.
 */
@Composable
fun ReveraTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val brandScheme =
        rememberDynamicColorScheme(
            seedColor = ReveraSeedColor,
            isDark = darkTheme,
            style = PaletteStyle.TonalSpot,
        )
    val platformScheme = platformDynamicColorScheme(darkTheme)
    val colorScheme = if (dynamicColor && platformScheme != null) platformScheme else brandScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = ReveraTypography,
        shapes = ReveraShapes,
    ) {
        CompositionLocalProvider(LocalReveraSpacing provides ReveraSpacing()) {
            content()
        }
    }
}

/** Access Revera-specific tokens alongside `MaterialTheme`. */
object ReveraTheme {
    val spacing: ReveraSpacing
        @Composable
        @ReadOnlyComposable
        get() = LocalReveraSpacing.current
}

/** The OS-provided dynamic scheme, or null where the platform has none. */
@Composable
internal expect fun platformDynamicColorScheme(darkTheme: Boolean): ColorScheme?
