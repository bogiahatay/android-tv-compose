package net.habui.tv.feature.settings.presentation.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged

@Composable
fun ColumnScope.FontScaleSelector(
    selectedScale: String,
    onScaleSelected: (String) -> Unit,
    startIndex: Int = 0,
    focusedItemIndex: Int = 0,
    focusedItemModifier: Modifier = Modifier,
    onItemFocused: (Int) -> Unit = {},
) {
    val options = remember { listOf("Small", "Medium", "Large") }

    options.forEachIndexed { localIdx, option ->
        val globalIdx = startIndex + localIdx
        SettingsSelectableItem(
            title = option,
            selected = selectedScale == option,
            onClick = { onScaleSelected(option) },
            modifier = Modifier
                .fillMaxWidth()
                .then(if (focusedItemIndex == globalIdx) focusedItemModifier else Modifier)
                .onFocusChanged { if (it.isFocused) onItemFocused(globalIdx) }
        )
    }
}
