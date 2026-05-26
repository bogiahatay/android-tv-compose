package net.habui.tv.core.designsystem

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.SurfaceDefaults

@Composable
fun TvAppTheme(
    themeConfig: TvThemeConfig = TvThemeConfig(),
    content: @Composable () -> Unit
) {
    val colorScheme by remember(themeConfig.themeMode, themeConfig.primaryColor) {
        derivedStateOf {
            tvColorScheme(
                darkTheme = themeConfig.themeMode == TvThemeMode.Dark,
                primaryColor = themeConfig.primaryColor
            )
        }
    }
    val typography by remember(themeConfig.fontScale) {
        derivedStateOf { tvTypography() }
    }
    val currentDensity = LocalDensity.current
    val scaledDensity = remember(currentDensity, themeConfig.fontScale) {
        Density(
            density = currentDensity.density,
            fontScale = currentDensity.fontScale * themeConfig.fontScale
        )
    }

    CompositionLocalProvider(
        LocalThemeConfig provides themeConfig,
        LocalDensity provides scaledDensity
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = typography
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                colors = SurfaceDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onBackground
                )
            ) {
                content()
            }
        }
    }
}
