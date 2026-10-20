package com.jj.revera.shared.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import com.jj.revera.core.designsystem.icons.ReveraIcons
import com.jj.revera.feature.home.api.HomeRoute
import com.jj.revera.feature.settings.api.SettingsRoute
import com.jj.revera.shared.resources.Res
import com.jj.revera.shared.resources.nav_home
import com.jj.revera.shared.resources.nav_settings
import org.jetbrains.compose.resources.StringResource

/** Destinations shown in the navigation bar/rail. Order here is display order. */
internal enum class TopLevelDestination(
    val route: NavKey,
    val label: StringResource,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
) {
    Home(HomeRoute, Res.string.nav_home, ReveraIcons.Home, ReveraIcons.HomeOutlined),
    Settings(SettingsRoute, Res.string.nav_settings, ReveraIcons.Settings, ReveraIcons.SettingsOutlined),
    ;

    companion object {
        val routes: Set<NavKey> = entries.mapTo(LinkedHashSet()) { it.route }
    }
}
