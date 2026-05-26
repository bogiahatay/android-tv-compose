package net.habui.tv.feature.home.presentation

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import net.habui.tv.core.focus.PositionFocusedItemInLazyLayout
import net.habui.tv.feature.home.components.ContentRow
import net.habui.tv.feature.home.components.FeaturedBanner
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    focusRestoreRequest: Int = 0,
    onMovieClick: (MovieUiModel) -> Unit,
) {

    val uiState by viewModel.uiState.observeAsState(
        initial = HomeUiState()
    )

    val focusedItemFocusRequester = remember {
        FocusRequester()
    }

    val columnListState = rememberSaveable(
        saver = LazyListState.Saver
    ) {
        LazyListState()
    }

    /**
     * -1 = banner
     * >= 0 = content row
     */
    var lastFocusedRowIndex by rememberSaveable {
        mutableIntStateOf(-1)
    }

    var lastFocusedItemIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    val hasFocusableFirstRowItem =
        uiState.sections.firstOrNull()
            ?.movies
            ?.isNotEmpty() == true

    val hasContent =
        uiState.featuredMovie != null ||
                uiState.sections.isNotEmpty()

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

    /**
     * Initial focus + restore focus
     */
    LaunchedEffect(
        focusRestoreRequest,
        uiState.featuredMovie,
        uiState.sections
    ) {

        when {

            /**
             * Restore content item
             */
            lastFocusedRowIndex in uiState.sections.indices && uiState.sections[lastFocusedRowIndex]
                .movies
                .isNotEmpty() -> {

                val columnIndex = lastFocusedRowIndex + if (uiState.featuredMovie != null) {
                    1
                } else {
                    0
                }

                columnListState.animateScrollToItem(
                    index = columnIndex
                )

                delay(100)
                runCatching {
                    focusedItemFocusRequester.requestFocus()
                }
            }

            /**
             * Restore banner
             */
            lastFocusedRowIndex == -1 -> {
                columnListState.animateScrollToItem(0)
            }

            /**
             * Fallback first row
             */
            hasFocusableFirstRowItem -> {
                focusedItemFocusRequester.requestFocus()
            }
        }
    }

    fun updateFocusedRow(
        rowIndex: Int,
        itemIndex: Int,
    ) {
        lastFocusedRowIndex = rowIndex
        lastFocusedItemIndex = itemIndex
    }

    PositionFocusedItemInLazyLayout(
        parentFraction = 0.5f,
        childFraction = 0.5f
    ) {

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = columnListState,
            verticalArrangement = Arrangement.spacedBy(32.dp),
            contentPadding = PaddingValues(
                vertical = 32.dp
            )
        ) {

            /**
             * Banner
             */
            uiState.featuredMovie?.let { movie ->

                item(
                    key = "featured_banner"
                ) {

                    FeaturedBanner(
                        movie = movie,
                        modifier = Modifier
                            .focusProperties {
                                down = focusedItemFocusRequester
                            },
                        onClick = {
                            onMovieClick(movie)
                        }
                    )
                }
            }

            /**
             * Content rows
             */
            itemsIndexed(
                items = uiState.sections,
                key = { _, section ->
                    section.title
                }
            ) { rowIndex, section ->

                ContentRow(
                    title = section.title,
                    rowIndex = rowIndex,
                    movies = section.movies,
                    onMovieClick = onMovieClick,

                    /**
                     * Restore focused item
                     */
                    focusedItemIndex =
                        if (rowIndex == lastFocusedRowIndex) {
                            lastFocusedItemIndex
                        } else {
                            0
                        },

                    /**
                     * Attach requester
                     */
                    focusedItemModifier =
                        if (rowIndex == lastFocusedRowIndex || (lastFocusedRowIndex == -1 && rowIndex == 0)) {
                            Modifier.focusRequester(
                                focusedItemFocusRequester
                            )
                        } else {
                            Modifier
                        },

                    /**
                     * Save focus immediately
                     */
                    onFocusedItemChanged = { itemIndex ->
                        updateFocusedRow(
                            rowIndex = rowIndex,
                            itemIndex = itemIndex
                        )
                    }
                )
            }

            /**
             * Retry section
             */
            if (uiState.error != null) {

                item(
                    key = "home_error_retry"
                ) {

                    InlineRetry(
                        message = uiState.error?.message.orEmpty(),
                        onRetryClick = {
                            viewModel.onAction(
                                HomeUiAction.OnRetryClick
                            )
                        }
                    )
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