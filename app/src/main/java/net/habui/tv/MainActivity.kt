package net.habui.tv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import dagger.hilt.android.AndroidEntryPoint
import net.habui.tv.navigation.AppNavHost
import net.habui.tv.core.designsystem.TvAppTheme
import net.habui.tv.feature.settings.presentation.SettingsUiState
import net.habui.tv.feature.settings.presentation.SettingsViewModel
import net.habui.tv.feature.settings.presentation.toThemeConfig

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val systemUiController = WindowCompat.getInsetsController(window, window.decorView)
        systemUiController.hide(WindowInsetsCompat.Type.systemBars())
        systemUiController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        setContent {
            val settingsUiState by settingsViewModel.uiState.observeAsState(SettingsUiState())
            val themeConfig by remember(
                settingsUiState.theme,
                settingsUiState.fontScale,
                settingsUiState.primaryColor
            ) {
                derivedStateOf { settingsUiState.toThemeConfig() }
            }

            TvAppTheme(themeConfig = themeConfig) {
                AppNavHost(settingsViewModel = settingsViewModel)
            }
        }
    }
}
