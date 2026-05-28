package net.habui.tv.feature.settings.presentation.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged

@Composable
fun ColumnScope.ThemeSelector(
    selectedTheme: String,
    onThemeSelected: (String) -> Unit,
    startIndex: Int = 0,
    focusedItemIndex: Int = 0,
    focusedItemModifier: Modifier = Modifier,
    onItemFocused: (Int) -> Unit = {},
) {
    listOf("Dark" to Icons.Default.DarkMode, "Light" to Icons.Default.LightMode)
        .forEachIndexed { localIdx, (title, icon) ->
            val globalIdx = startIndex + localIdx
            SettingsSelectableItem(
                title = title,
                selected = selectedTheme == title,
                onClick = { onThemeSelected(title) },
                leadingIcon = icon,
                modifier = Modifier
                    .fillMaxWidth()
                    .then(if (focusedItemIndex == globalIdx) focusedItemModifier else Modifier)
                    .onFocusChanged { if (it.isFocused) onItemFocused(globalIdx) }
            )
        }
}
