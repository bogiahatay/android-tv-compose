package net.habui.tv.feature.home.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Border
import androidx.tv.material3.ClickableSurfaceDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import net.habui.tv.core.designsystem.tvFocusBorder
import net.habui.tv.core.designsystem.tvImageError
import net.habui.tv.core.designsystem.tvImagePlaceholder
import net.habui.tv.core.designsystem.tvOnPlayerOverlay
import net.habui.tv.core.designsystem.tvPlayerOverlayScrim
import coil.compose.AsyncImage
import coil.request.ImageRequest
import net.habui.tv.feature.home.presentation.MovieUiModel

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun FeaturedBanner(
    movie: MovieUiModel,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val context = LocalContext.current

    Surface(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(400.dp)
            .padding(horizontal = 48.dp),
        scale = ClickableSurfaceDefaults.scale(focusedScale = 1.05f),
        border = ClickableSurfaceDefaults.border(
            focusedBorder = Border(
                border = BorderStroke(3.dp, MaterialTheme.colorScheme.tvFocusBorder)
            )
        ),
        colors = ClickableSurfaceDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurface,
            focusedContainerColor = MaterialTheme.colorScheme.inverseSurface,
            focusedContentColor = MaterialTheme.colorScheme.inverseOnSurface
        ),
        shape = ClickableSurfaceDefaults.shape(
            shape = MaterialTheme.shapes.medium
        )
    ) {
        Box {
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
                            colors = listOf(
                                Color.Transparent,
                                MaterialTheme.colorScheme.tvPlayerOverlayScrim.copy(alpha = 0.72f)
                            )
                        )
                    )
            )

            Text(
                text = movie.title,
                modifier = Modifier.padding(32.dp),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.tvOnPlayerOverlay
            )
        }
    }
}
