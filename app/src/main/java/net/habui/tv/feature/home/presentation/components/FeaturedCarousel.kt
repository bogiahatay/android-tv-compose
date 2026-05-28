package net.habui.tv.feature.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import androidx.tv.material3.Carousel
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.OutlinedButton
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import coil.request.ImageRequest
import net.habui.tv.core.designsystem.TvAppTheme
import net.habui.tv.core.designsystem.tvImageError
import net.habui.tv.core.designsystem.tvImagePlaceholder
import net.habui.tv.core.designsystem.tvOnPlayerOverlay
import net.habui.tv.core.designsystem.tvPlayerOverlayScrim
import net.habui.tv.feature.home.presentation.FeaturedMovieUiModel
import net.habui.tv.feature.home.presentation.PlaybackType

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun FeaturedCarousel(
    movies: List<FeaturedMovieUiModel>,
    modifier: Modifier = Modifier,
    onPlayClick: (FeaturedMovieUiModel) -> Unit = {},
    onDetailsClick: (FeaturedMovieUiModel) -> Unit = {},
) {
    if (movies.isEmpty()) return

    Carousel(
        itemCount = movies.size,
        modifier = modifier
            .fillMaxWidth()
            .height(400.dp)
            .padding(horizontal = 48.dp)
            .clip(MaterialTheme.shapes.medium),
        autoScrollDurationMillis = 6_000L,
    ) { index ->
        val movie = movies[index]
        CarouselItem(
            movie = movie,
            onPlayClick = { onPlayClick(movie) },
            onDetailsClick = { onDetailsClick(movie) }
        )
    }
}

@Composable
private fun CarouselItem(
    movie: FeaturedMovieUiModel,
    onPlayClick: () -> Unit,
    onDetailsClick: () -> Unit,
) {
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {

        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(movie.imageUrl)
                .crossfade(false)
                .build(),
            contentDescription = movie.title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            placeholder = ColorPainter(MaterialTheme.colorScheme.tvImagePlaceholder),
            error = ColorPainter(MaterialTheme.colorScheme.tvImageError)
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0f to Color.Transparent,
                        0.4f to MaterialTheme.colorScheme.tvPlayerOverlayScrim.copy(alpha = 0.3f),
                        1f to MaterialTheme.colorScheme.tvPlayerOverlayScrim.copy(alpha = 0.85f)
                    )
                )
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(horizontal = 40.dp, vertical = 36.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text(
                text = movie.title,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.tvOnPlayerOverlay,
                maxLines = 2
            )

            if (movie.description.isNotBlank()) {
                Text(
                    text = movie.description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.tvOnPlayerOverlay.copy(alpha = 0.8f),
                    maxLines = 2
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Button(
                    onClick = onPlayClick
                ) {
                    Text(text = "Play")
                }

                OutlinedButton(onClick = onDetailsClick) {
                    Text(text = "Details")
                }
            }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Preview(device = "id:tv_1080p")
@Composable
private fun FeaturedCarouselPreview() {
    TvAppTheme {
        FeaturedCarousel(
            movies = listOf(
                FeaturedMovieUiModel(
                    id = "1",
                    title = "Oppenheimer",
                    description = "The story of J. Robert Oppenheimer and his role in the development of the atomic bomb.",
                    imageUrl = "",
                    videoUrl = "",
                    playbackType = PlaybackType.Vod
                ),
                FeaturedMovieUiModel(
                    id = "2",
                    title = "Dune: Part Two",
                    description = "Paul Atreides unites with Chani and the Fremen while seeking revenge against the conspirators.",
                    imageUrl = "",
                    videoUrl = "",
                    playbackType = PlaybackType.Vod
                )
            )
        )
    }
}
