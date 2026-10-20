package com.jj.revera.feature.settings.impl

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jj.revera.core.designsystem.components.ReveraSectionHeader
import com.jj.revera.core.designsystem.components.readableWidth
import com.jj.revera.core.designsystem.icons.ReveraIcons
import com.jj.revera.core.designsystem.theme.ReveraTheme
import com.jj.revera.core.model.ThemeMode
import com.jj.revera.core.ui.adaptive.FoldPosture
import com.jj.revera.core.ui.adaptive.LocalReveraWindowInfo
import com.jj.revera.core.ui.adaptive.ReveraWindowInfo
import com.jj.revera.feature.settings.impl.resources.Res
import com.jj.revera.feature.settings.impl.resources.settings_dynamic_color
import com.jj.revera.feature.settings.impl.resources.settings_dynamic_color_summary
import com.jj.revera.feature.settings.impl.resources.settings_platform
import com.jj.revera.feature.settings.impl.resources.settings_posture_book
import com.jj.revera.feature.settings.impl.resources.settings_posture_flat
import com.jj.revera.feature.settings.impl.resources.settings_posture_tabletop
import com.jj.revera.feature.settings.impl.resources.settings_section_about
import com.jj.revera.feature.settings.impl.resources.settings_section_appearance
import com.jj.revera.feature.settings.impl.resources.settings_section_window
import com.jj.revera.feature.settings.impl.resources.settings_theme
import com.jj.revera.feature.settings.impl.resources.settings_theme_dark
import com.jj.revera.feature.settings.impl.resources.settings_theme_light
import com.jj.revera.feature.settings.impl.resources.settings_theme_system
import com.jj.revera.feature.settings.impl.resources.settings_title
import com.jj.revera.feature.settings.impl.resources.settings_version
import com.jj.revera.feature.settings.impl.resources.settings_window_height
import com.jj.revera.feature.settings.impl.resources.settings_window_panes
import com.jj.revera.feature.settings.impl.resources.settings_window_posture
import com.jj.revera.feature.settings.impl.resources.settings_window_width
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun SettingsRoute(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    SettingsScreen(
        uiState = uiState,
        windowInfo = LocalReveraWindowInfo.current,
        onThemeModeChange = viewModel::setThemeMode,
        onDynamicColorChange = viewModel::setUseDynamicColor,
        modifier = modifier,
    )
}

@Composable
internal fun SettingsScreen(
    uiState: SettingsUiState,
    windowInfo: ReveraWindowInfo,
    onThemeModeChange: (ThemeMode) -> Unit,
    onDynamicColorChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = { TopAppBar(title = { Text(stringResource(Res.string.settings_title)) }) },
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(bottom = ReveraTheme.spacing.xl),
        ) {
            item {
                ReveraSectionHeader(
                    stringResource(Res.string.settings_section_appearance),
                    Modifier.readableWidth(),
                )
            }
            item { ThemeModeSetting(uiState.themeMode, onThemeModeChange, Modifier.readableWidth()) }
            if (uiState.supportsDynamicColor) {
                item {
                    DynamicColorSetting(uiState.useDynamicColor, onDynamicColorChange, Modifier.readableWidth())
                }
            }

            item { ReveraSectionHeader(stringResource(Res.string.settings_section_window), Modifier.readableWidth()) }
            item { InfoRow(ReveraIcons.Devices, Res.string.settings_window_width, windowInfo.widthClass.name) }
            item { InfoRow(null, Res.string.settings_window_height, windowInfo.heightClass.name) }
            item { InfoRow(null, Res.string.settings_window_posture, stringResource(windowInfo.posture.label())) }
            item { InfoRow(null, Res.string.settings_window_panes, windowInfo.maxHorizontalPanes.toString()) }

            item { ReveraSectionHeader(stringResource(Res.string.settings_section_about), Modifier.readableWidth()) }
            item { InfoRow(ReveraIcons.Info, Res.string.settings_version, uiState.version) }
            item { InfoRow(null, Res.string.settings_platform, uiState.platformDescription) }
        }
    }
}

@Composable
private fun ThemeModeSetting(
    selected: ThemeMode,
    onSelect: (ThemeMode) -> Unit,
    modifier: Modifier = Modifier,
) {
    ListItem(
        modifier = modifier,
        leadingContent = { Icon(ReveraIcons.Palette, contentDescription = null) },
        headlineContent = { Text(stringResource(Res.string.settings_theme)) },
        supportingContent = {
            SingleChoiceSegmentedButtonRow(
                modifier = Modifier.fillMaxWidth().padding(top = ReveraTheme.spacing.sm),
            ) {
                ThemeMode.entries.forEachIndexed { index, mode ->
                    SegmentedButton(
                        selected = mode == selected,
                        onClick = { onSelect(mode) },
                        shape = SegmentedButtonDefaults.itemShape(index = index, count = ThemeMode.entries.size),
                    ) {
                        Text(stringResource(mode.label()))
                    }
                }
            }
        },
    )
}

@Composable
private fun DynamicColorSetting(
    enabled: Boolean,
    onChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    ListItem(
        modifier = modifier.toggleable(value = enabled, onValueChange = onChange, role = Role.Switch),
        leadingContent = { Icon(ReveraIcons.Palette, contentDescription = null) },
        headlineContent = { Text(stringResource(Res.string.settings_dynamic_color)) },
        supportingContent = { Text(stringResource(Res.string.settings_dynamic_color_summary)) },
        trailingContent = { Switch(checked = enabled, onCheckedChange = null) },
    )
}

@Composable
private fun InfoRow(
    icon: ImageVector?,
    label: StringResource,
    value: String,
) {
    ListItem(
        modifier = Modifier.readableWidth(),
        leadingContent = { Icon(icon ?: ReveraIcons.Info, contentDescription = null, tint = iconTint(icon)) },
        headlineContent = { Text(stringResource(label)) },
        supportingContent = { Text(value, style = MaterialTheme.typography.bodyMedium) },
    )
}

/** Rows without their own icon keep the column aligned with an invisible placeholder. */
@Composable
private fun iconTint(icon: ImageVector?) =
    if (icon == null) {
        Color.Transparent
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

private fun ThemeMode.label(): StringResource =
    when (this) {
        ThemeMode.System -> Res.string.settings_theme_system
        ThemeMode.Light -> Res.string.settings_theme_light
        ThemeMode.Dark -> Res.string.settings_theme_dark
    }

private fun FoldPosture.label(): StringResource =
    when (this) {
        FoldPosture.Flat -> Res.string.settings_posture_flat
        is FoldPosture.Tabletop -> Res.string.settings_posture_tabletop
        is FoldPosture.Book -> Res.string.settings_posture_book
    }
