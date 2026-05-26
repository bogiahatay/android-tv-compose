package net.habui.tv.feature.settings.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ColumnScope.ThemeSelector(
    selectedTheme: String,
    onThemeSelected: (String) -> Unit,
    firstItemModifier: Modifier = Modifier
) {
    SettingsSelectableItem(
        title = "Dark",
        selected = selectedTheme == "Dark",
        onClick = { onThemeSelected("Dark") },
        leadingIcon = Icons.Default.DarkMode,
        modifier = firstItemModifier.fillMaxWidth()
    )
    SettingsSelectableItem(
        title = "Light",
        selected = selectedTheme == "Light",
        onClick = { onThemeSelected("Light") },
        leadingIcon = Icons.Default.LightMode,
        modifier = Modifier.fillMaxWidth()
    )
}
