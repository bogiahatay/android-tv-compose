package net.habui.tv.feature.player.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.tv.material3.LocalContentColor
import androidx.tv.material3.MaterialTheme
import net.habui.tv.core.designsystem.tvOnPlayerOverlay
import net.habui.tv.core.designsystem.tvPlayerOverlayScrim

@Composable
fun PlayerOverlay(
    visible: Boolean,
    onFocusChanged: (Boolean) -> Unit = {},
    content: @Composable BoxScope.() -> Unit
) {
    val overlayScrim = MaterialTheme.colorScheme.tvPlayerOverlayScrim

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(durationMillis = 500)),
        exit = fadeOut(animationSpec = tween(durationMillis = 500))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .onFocusChanged { onFocusChanged(it.hasFocus) }
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            overlayScrim.copy(alpha = 0.55f),
                            Color.Transparent,
                            overlayScrim.copy(alpha = 0.75f)
                        )
                    )
                ),
        ) {
            CompositionLocalProvider(
                LocalContentColor provides MaterialTheme.colorScheme.tvOnPlayerOverlay
            ) {
                content()
            }
        }
    }
}
