package net.habui.tv.feature.settings.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import net.habui.tv.core.designsystem.TvAppTheme

@Composable
fun SettingsHeader(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        style = MaterialTheme.typography.headlineMedium,
        modifier = modifier.padding(bottom = 32.dp)
    )
}

@Preview
@Composable
private fun SettingsHeaderPreview() {
    TvAppTheme {
        SettingsHeader(title = "Settings")
    }
}
