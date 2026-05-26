package net.habui.tv.feature.settings.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Border
import androidx.tv.material3.ListItem
import androidx.tv.material3.ListItemDefaults
import androidx.tv.material3.LocalContentColor
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Switch
import androidx.tv.material3.Text
import net.habui.tv.core.designsystem.TvAppTheme
import net.habui.tv.core.designsystem.tvFocusBorder

@Composable
fun SettingsToggleItem(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    subtitle: String? = null
) {
    ListItem(
        selected = false,
        onClick = { onCheckedChange(!checked) },
        headlineContent = {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge
            )
        },
        supportingContent = subtitle?.let {
            {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = LocalContentColor.current.copy(alpha = 0.78f)
                )
            }
        },
        trailingContent = {
            Switch(
                checked = checked,
                onCheckedChange = null // Handled by ListItem onClick
            )
        },
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
            focusedContainerColor = MaterialTheme.colorScheme.inverseSurface,
            focusedContentColor = MaterialTheme.colorScheme.inverseOnSurface,
            pressedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            pressedContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        border = ListItemDefaults.border(
            focusedBorder = Border(
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.tvFocusBorder)
            )
        ),
        modifier = modifier.fillMaxWidth()
    )
}

@Preview
@Composable
private fun SettingsToggleItemPreview() {
    TvAppTheme {
        SettingsToggleItem(
            title = "Auto Play",
            subtitle = "Automatically play the next episode",
            checked = true,
            onCheckedChange = {}
        )
    }
}
