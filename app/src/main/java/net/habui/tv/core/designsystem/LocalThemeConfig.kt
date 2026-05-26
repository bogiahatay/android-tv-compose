package net.habui.tv.core.designsystem

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

enum class TvThemeMode {
    Dark,
    Light
}

@Immutable
data class TvThemeConfig(
    val themeMode: TvThemeMode = TvThemeMode.Dark,
    val fontScale: Float = 1f,
    val primaryColor: Color = TvPrimaryBlue,
    val spacing: TvSpacing = TvSpacing()
)

val LocalThemeConfig = staticCompositionLocalOf { TvThemeConfig() }
