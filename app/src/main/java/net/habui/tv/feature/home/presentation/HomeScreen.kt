package net.habui.tv.feature.home.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.layout.LazyLayoutCacheWindow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import net.habui.tv.core.focus.PositionFocusedItemInLazyLayout
import net.habui.tv.feature.home.presentation.components.ContentRow
import net.habui.tv.feature.home.presentation.components.FeaturedCarousel
import timber.log.Timber

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onMovieClick: (MovieUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {

    val uiState by viewModel.uiState.observeAsState(
        initial = HomeUiState()
    )

    var lastFocusedRowIndex by rememberSaveable { mutableIntStateOf(-1) } // -1 = carousel
    var lastFocusedItemIndex by rememberSaveable { mutableIntStateOf(0) }
    val focusedItemRequester = remember { FocusRequester() }
    val listState = rememberLazyListState(
        cacheWindow = LazyLayoutCacheWindow(ahead = 500.dp, behind = 300.dp)
    )

    val hasContent = uiState.featuredMovies.isNotEmpty() || uiState.sections.isNotEmpty()
    if (!hasContent && uiState.isLoading) {
        FullScreenMessage(
            message = "Loading..."
        )
        return
    }

    if (!hasContent && uiState.error == HomeUiError.Empty) {
        FullScreenMessage(
            message = uiState.error?.message.orEmpty()
        )
        return
    }

    if (!hasContent && uiState.error != null) {
        FullScreenRetry(
            message = uiState.error?.message.orEmpty(),
            onRetryClick = {
                viewModel.onAction(
                    HomeUiAction.OnRetryClick
                )
            }
        )
        return
    }

    LaunchedEffect(Unit) {
        focusedItemRequester.requestFocus()
        Timber.tag("TV_FOCUS").d("HomeScreen focusedItemRequester")
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .focusProperties {
                onEnter = {
                    focusedItemRequester.requestFocus()
                    Timber.tag("TV_FOCUS").d("HomeScreen focusedItemRequester")
                    FocusRequester.Cancel
                }
            }
            .focusGroup()
    ) {
        PositionFocusedItemInLazyLayout(targetOffsetYPx = 300f) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = listState,
                verticalArrangement = Arrangement.spacedBy(32.dp),
                contentPadding = PaddingValues(vertical = 32.dp),
            ) {

                if (uiState.featuredMovies.isNotEmpty()) {
                    item(key = "featured_carousel") {
                        val isCarouselFocused by remember { derivedStateOf { lastFocusedRowIndex == -1 } }
                        FeaturedCarousel(
                            modifier = Modifier
                                .then(
                                    if (isCarouselFocused) Modifier.focusRequester(focusedItemRequester)
                                    else Modifier
                                )
                                .onFocusChanged { focusState ->
                                    if (focusState.hasFocus) lastFocusedRowIndex = -1
                                },
                            movies = uiState.featuredMovies,
                            onPlayClick = { featured -> onMovieClick(featured.toMovieUiModel()) },
                            onDetailsClick = { featured -> onMovieClick(featured.toMovieUiModel()) }
                        )
                    }
                }

                itemsIndexed(
                    items = uiState.sections,
                    key = { _, section -> section.title }
                ) { rowIndex, section ->

                    val isFocusedRow by remember { derivedStateOf { rowIndex == lastFocusedRowIndex } }
                    val focusedIndex by remember { derivedStateOf {
                        if (rowIndex == lastFocusedRowIndex) lastFocusedItemIndex else 0
                    } }

                    ContentRow(
                        title = section.title,
                        rowIndex = rowIndex,
                        movies = section.movies,
                        onMovieClick = onMovieClick,
                        focusedItemIndex = focusedIndex,
                        focusedItemModifier = if (isFocusedRow) {
                            Modifier.focusRequester(focusedItemRequester)
                        } else {
                            Modifier
                        },
                        onFocusedItemChanged = { itemIndex ->
                            lastFocusedRowIndex = rowIndex
                            lastFocusedItemIndex = itemIndex
                        }
                    )
                }

                if (uiState.error != null) {
                    item(key = "home_error_retry") {
                        InlineRetry(
                            message = uiState.error?.message.orEmpty(),
                            onRetryClick = {
                                viewModel.onAction(HomeUiAction.OnRetryClick)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FullScreenMessage(
    message: String,
) {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = message,
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Composable
private fun FullScreenRetry(
    message: String,
    onRetryClick: () -> Unit,
) {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Button(
            onClick = onRetryClick
        ) {

            Text(
                text = "$message Retry"
            )
        }
    }
}

@Composable
private fun InlineRetry(
    message: String,

    onRetryClick: () -> Unit,
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 48.dp),
        contentAlignment = Alignment.CenterStart
    ) {

        Button(
            onClick = onRetryClick
        ) {

            Text(
                text = "$message Retry"
            )
        }
    }
}