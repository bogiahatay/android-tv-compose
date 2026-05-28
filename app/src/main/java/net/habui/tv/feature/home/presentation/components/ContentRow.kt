package net.habui.tv.feature.home.presentation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyListPrefetchStrategy
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import net.habui.tv.core.focus.AlwaysAnchorFocusedItemInLazyLayout
import net.habui.tv.feature.home.presentation.MovieUiModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ContentRow(
    title: String,
    rowIndex: Int,
    movies: List<MovieUiModel>,
    onMovieClick: (MovieUiModel) -> Unit,
    focusedItemIndex: Int = 0,
    focusedItemModifier: Modifier = Modifier,
    onFocusedItemChanged: (Int) -> Unit = {},
) {


    val density = LocalDensity.current
    val windowWidth = LocalWindowInfo.current.containerSize.width

    val anchorOffsetPx = remember(density) { with(density) { 48.dp.toPx() } }
    val endPadding = remember(density, windowWidth) { with(density) { windowWidth.toDp() - 48.dp } }
    val lazyRowState = rememberLazyListState(
        prefetchStrategy = remember { LazyListPrefetchStrategy(nestedPrefetchItemCount = 5) }
    )


    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        SectionTitle(title)

        AlwaysAnchorFocusedItemInLazyLayout(
            targetOffsetPx = anchorOffsetPx,
        ) {

            LazyRow(
                state = lazyRowState,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(
                    start = 48.dp,
                    end = endPadding
                )
            ) {

                itemsIndexed(
                    items = movies,
                    key = { _, movie -> movie.id }
                ) { index, movie ->
                    MovieCard(
                        movie = movie,
                        onClick = {
                            onMovieClick(movie)
                        },
                        modifier = Modifier
                            .then(
                                if (index == focusedItemIndex.coerceAtMost(movies.lastIndex)) {
                                    focusedItemModifier
                                } else {
                                    Modifier
                                }
                            )
                            .onFocusChanged { focusState ->
                                if (focusState.isFocused) {
                                    onFocusedItemChanged(index)
                                }
                            }
                    )
                }
            }
        }
    }
}
