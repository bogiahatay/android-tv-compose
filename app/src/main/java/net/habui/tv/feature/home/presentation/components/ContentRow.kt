package net.habui.tv.feature.home.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import net.habui.tv.core.focus.AlwaysAnchorFocusedItemInLazyLayout
import net.habui.tv.feature.home.presentation.MovieUiModel

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

    val listState = rememberSaveable(rowIndex, saver = LazyListState.Saver) {
        LazyListState()
    }

    val density = LocalDensity.current

    /**
     * Fixed anchor position where focused items should align.
     */
    val anchorOffset = 48.dp

    val anchorOffsetPx = with(density) {
        anchorOffset.toPx()
    }

    /**
     * Current viewport width.
     */
    val viewportWidth = with(density) {
        LocalWindowInfo.current.containerSize.width.toDp()
    }

    /**
     * Add enough trailing space so even the last item
     * can still scroll into the anchor position.
     */
    val endPadding = viewportWidth - anchorOffset


    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        SectionTitle(title)

        AlwaysAnchorFocusedItemInLazyLayout(
            targetOffsetPx = anchorOffsetPx,
        ) {

            LazyRow(
                state = listState,
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
