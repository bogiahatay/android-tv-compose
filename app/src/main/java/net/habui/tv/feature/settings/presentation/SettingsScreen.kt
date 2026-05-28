package net.habui.tv.feature.settings.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import net.habui.tv.core.designsystem.TvAppTheme
import net.habui.tv.core.focus.PositionFocusedItemInLazyLayout
import net.habui.tv.feature.settings.presentation.components.ColorSelector
import net.habui.tv.feature.settings.presentation.components.FontScaleSelector
import net.habui.tv.feature.settings.presentation.components.SettingsSection
import net.habui.tv.feature.settings.presentation.components.ThemeSelector
import timber.log.Timber

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.observeAsState(SettingsUiState())

    SettingsContent(
        uiState = uiState,
        onAction = viewModel::onAction,
    )
}

@Composable
private fun SettingsContent(
    uiState: SettingsUiState,
    onAction: (SettingsUiAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    var lastFocusedItemIndex by rememberSaveable { mutableIntStateOf(0) }
    val focusedItemRequester = remember { FocusRequester() }
    val focusedItemModifier = Modifier.focusRequester(focusedItemRequester)

    LaunchedEffect(Unit) {
        focusedItemRequester.requestFocus()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .focusProperties {
                onEnter = {
                    Timber.tag("SettingsContent").d("Box onEnter ")
                    focusedItemRequester.requestFocus()
                    FocusRequester.Cancel
                }
            }
            .focusGroup()
    ) {
        PositionFocusedItemInLazyLayout(
            parentFraction = 0.3f,
            childFraction = 0.5f
        ) {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .widthIn(max = 900.dp),
                contentPadding = PaddingValues(start = 56.dp, top = 46.dp, end = 72.dp, bottom = 72.dp)
            ) {
                item {
                    Text(
                        text = "Settings",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    SettingsSection(title = "Theme") {
                        ThemeSelector(
                            selectedTheme = uiState.theme,
                            onThemeSelected = { onAction(SettingsUiAction.ChangeTheme(it)) },
                            startIndex = 0,
                            focusedItemIndex = lastFocusedItemIndex,
                            focusedItemModifier = focusedItemModifier,
                            onItemFocused = { lastFocusedItemIndex = it }
                        )
                    }
                }

                item {
                    SettingsSection(title = "Color mode") {
                        ColorSelector(
                            selectedColor = uiState.primaryColor,
                            onColorSelected = { onAction(SettingsUiAction.ChangePrimaryColor(it)) },
                            startIndex = 2,
                            focusedItemIndex = lastFocusedItemIndex,
                            focusedItemModifier = focusedItemModifier,
                            onItemFocused = { lastFocusedItemIndex = it }
                        )
                    }
                }

                item {
                    SettingsSection(title = "Font scale") {
                        FontScaleSelector(
                            selectedScale = uiState.fontScale,
                            onScaleSelected = { onAction(SettingsUiAction.ChangeFontScale(it)) },
                            startIndex = 7,
                            focusedItemIndex = lastFocusedItemIndex,
                            focusedItemModifier = focusedItemModifier,
                            onItemFocused = { lastFocusedItemIndex = it }
                        )
                    }
                }
            }
        }
    }
}

@Preview(device = "id:tv_1080p")
@Composable
private fun SettingsScreenPreview() {
    TvAppTheme {
        SettingsContent(
            uiState = SettingsUiState(),
            onAction = {},
        )
    }
}
