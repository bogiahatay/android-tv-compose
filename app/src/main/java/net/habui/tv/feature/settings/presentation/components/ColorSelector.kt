package net.habui.tv.feature.settings.presentation.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import net.habui.tv.core.designsystem.TvPrimaryAegean
import net.habui.tv.core.designsystem.TvPrimaryBlue
import net.habui.tv.core.designsystem.TvPrimaryDijon
import net.habui.tv.core.designsystem.TvPrimaryRosewood
import net.habui.tv.core.designsystem.TvPrimaryShamrock

@Composable
fun ColumnScope.ColorSelector(
    selectedColor: String,
    onColorSelected: (String) -> Unit,
    startIndex: Int = 0,
    focusedItemIndex: Int = 0,
    focusedItemModifier: Modifier = Modifier,
    onItemFocused: (Int) -> Unit = {},
) {
    val options = remember {
        listOf(
            ColorOption("Default", TvPrimaryBlue),
            ColorOption("Shamrock", TvPrimaryShamrock),
            ColorOption("Aegean", TvPrimaryAegean),
            ColorOption("Rosewood", TvPrimaryRosewood),
            ColorOption("Dijon", TvPrimaryDijon)
        )
    }

    options.forEachIndexed { localIdx, option ->
        val globalIdx = startIndex + localIdx
        SettingsSelectableItem(
            title = option.title,
            selected = selectedColor == option.title,
            onClick = { onColorSelected(option.title) },
            indicatorColor = option.color,
            modifier = Modifier
                .fillMaxWidth()
                .then(if (focusedItemIndex == globalIdx) focusedItemModifier else Modifier)
                .onFocusChanged { if (it.isFocused) onItemFocused(globalIdx) }
        )
    }
}

private data class ColorOption(
    val title: String,
    val color: Color
)
