package net.habui.tv.feature.home.presentation.components

import android.graphics.Bitmap
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Border
import androidx.tv.material3.Card
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.size.Precision
import net.habui.tv.core.designsystem.tvFocusBorder
import net.habui.tv.core.designsystem.tvImageError
import net.habui.tv.core.designsystem.tvImagePlaceholder
import net.habui.tv.feature.home.presentation.MovieUiModel

private val CardWidth = 180.dp
private const val CardAspectRatio = 16f / 9f

@Composable
fun MovieCard(
    movie: MovieUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current
    val density = LocalDensity.current

    /**
     * Match decode size to actual render size.
     * Important for Android TV performance.
     */
    val widthPx = with(density) {
        CardWidth.roundToPx()
    }

    val heightPx = with(density) {
        (CardWidth / CardAspectRatio).roundToPx()
    }

    val placeholderColor = MaterialTheme.colorScheme.tvImagePlaceholder
    val errorColor = MaterialTheme.colorScheme.tvImageError

    val placeholderPainter = remember(placeholderColor) {
        ColorPainter(placeholderColor)
    }

    val errorPainter = remember(errorColor) {
        ColorPainter(errorColor)
    }

    val imageRequest = remember(
        movie.imageUrl,
        widthPx,
        heightPx
    ) {
        ImageRequest.Builder(context)
            .data(movie.imageUrl)

            /**
             * TV optimization
             */
            .crossfade(false)
            .allowHardware(true)
            .precision(Precision.INEXACT)

            /**
             * Decode only needed size
             */
            .size(widthPx, heightPx)

            /**
             * Better memory usage for TV rails
             */
            .bitmapConfig(Bitmap.Config.RGB_565)

            /**
             * Stable caching
             */
            .memoryCacheKey(movie.imageUrl)
            .diskCacheKey(movie.imageUrl)

            /**
             * Keep caches enabled
             */
            .memoryCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.ENABLED)
            .networkCachePolicy(CachePolicy.ENABLED)

            .build()
    }

    Card(
        onClick = onClick,
        modifier = modifier.width(CardWidth),
        border = CardDefaults.border(
            focusedBorder = Border(
                border = BorderStroke(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.tvFocusBorder
                )
            )
        ),
        colors = CardDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            focusedContainerColor = MaterialTheme.colorScheme.inverseSurface,
            focusedContentColor = MaterialTheme.colorScheme.inverseOnSurface
        )
    ) {

        Column {

            AsyncImage(
                model = imageRequest,
                contentDescription = movie.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(CardAspectRatio)
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop,
                placeholder = placeholderPainter,
                error = errorPainter,
                filterQuality = FilterQuality.Low,
            )

            Text(
                text = movie.title,
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.labelMedium,
                maxLines = 1
            )
        }
    }
}
