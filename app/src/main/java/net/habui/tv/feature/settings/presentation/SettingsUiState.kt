package net.habui.tv.feature.settings.presentation

import net.habui.tv.core.designsystem.TvPrimaryBlue
import net.habui.tv.core.designsystem.TvPrimaryAegean
import net.habui.tv.core.designsystem.TvPrimaryDijon
import net.habui.tv.core.designsystem.TvPrimaryRosewood
import net.habui.tv.core.designsystem.TvPrimaryShamrock
import net.habui.tv.core.designsystem.TvThemeConfig
import net.habui.tv.core.designsystem.TvThemeMode

data class SettingsUiState(
    val autoPlay: Boolean = true,
    val videoQuality: String = "Auto",
    val subtitle: Boolean = true,
    val audioLanguage: String = "English",
    val theme: String = "Dark",
    val fontScale: String = "Medium",
    val primaryColor: String = "Default",
    val reducedAnimation: Boolean = false,
    val appVersion: String = "1.0.0 (1)"
)

fun SettingsUiState.toThemeConfig(): TvThemeConfig {
    return TvThemeConfig(
        themeMode = when (theme) {
            "Light" -> TvThemeMode.Light
            else -> TvThemeMode.Dark
        },
        fontScale = when (fontScale) {
            "Small" -> 0.9f
            "Large" -> 1.15f
            else -> 1f
        },
        primaryColor = when (primaryColor) {
            "Shamrock" -> TvPrimaryShamrock
            "Aegean" -> TvPrimaryAegean
            "Rosewood" -> TvPrimaryRosewood
            "Dijon" -> TvPrimaryDijon
            else -> TvPrimaryBlue
        }
    )
}
