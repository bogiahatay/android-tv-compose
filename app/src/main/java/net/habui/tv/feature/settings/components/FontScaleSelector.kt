package net.habui.tv.feature.settings.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun ColumnScope.FontScaleSelector(
    selectedScale: String,
    onScaleSelected: (String) -> Unit
) {
    val options = remember { listOf("Small", "Medium", "Large") }

    options.forEach { option ->
        SettingsSelectableItem(
            title = option,
            selected = selectedScale == option,
            onClick = { onScaleSelected(option) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
