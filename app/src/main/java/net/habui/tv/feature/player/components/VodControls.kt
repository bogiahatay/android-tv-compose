package net.habui.tv.feature.player.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import androidx.tv.material3.Border
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.Icon
import androidx.tv.material3.LocalContentColor
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import net.habui.tv.core.designsystem.tvFocusBorder
import net.habui.tv.core.designsystem.tvLiveContainer
import net.habui.tv.feature.player.presentation.PlayerUiState
import kotlin.math.roundToLong

@Composable
fun VodControls(
    uiState: PlayerUiState,
    player: Player,
    playPauseFocusRequester: FocusRequester,
    onUserInteraction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(48.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = uiState.title,
            style = MaterialTheme.typography.headlineMedium
        )

        FocusableSeekBar(
            positionMs = uiState.positionMs,
            durationMs = uiState.durationMs,
            onUserInteraction = onUserInteraction,
            onSeek = { positionMs ->
                player.seekTo(positionMs)
            }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(28.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ControlButton(
                    icon = Icons.Default.FastRewind,
                    contentDescription = "Rewind"
                ) {
                    onUserInteraction()
                    player.seekTo((player.currentPosition - 10_000L).coerceAtLeast(0L))
                }

                ControlButton(
                    icon = if (uiState.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (uiState.isPlaying) "Pause" else "Play",
                    modifier = Modifier.focusRequester(playPauseFocusRequester),
                    onClick = {
                        onUserInteraction()
                        if (player.isPlaying) {
                            player.pause()
                        } else {
                            player.play()
                        }
                    }
                )

                ControlButton(
                    icon = Icons.Default.FastForward,
                    contentDescription = "Forward"
                ) {
                    onUserInteraction()
                    val duration = player.duration.takeIf { it > 0 } ?: Long.MAX_VALUE
                    player.seekTo((player.currentPosition + 10_000L).coerceAtMost(duration))
                }
            }
        }

        Text(
            text = "${formatTime(uiState.positionMs)} / ${formatTime(uiState.durationMs)}",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun FocusableSeekBar(
    positionMs: Long,
    durationMs: Long,
    onUserInteraction: () -> Unit,
    onSeek: (Long) -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }
    val progress = if (durationMs > 0L) {
        (positionMs.toFloat() / durationMs.toFloat()).coerceIn(0f, 1f)
    } else {
        0f
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(24.dp)
            .border(
                width = if (isFocused) 2.dp else 0.dp,
                color = if (isFocused) {
                    MaterialTheme.colorScheme.tvFocusBorder
                } else {
                    Color.Transparent
                },
                shape = RoundedCornerShape(4.dp)
            )
            .padding(horizontal = 4.dp, vertical = 8.dp)
            .onFocusChanged { isFocused = it.isFocused }
            .onKeyEvent { event ->
                if (event.type != KeyEventType.KeyDown || durationMs <= 0L) {
                    return@onKeyEvent false
                }

                val stepMs = 10_000L
                when (event.key) {
                    Key.DirectionLeft -> {
                        onUserInteraction()
                        onSeek((positionMs - stepMs).coerceAtLeast(0L))
                        true
                    }

                    Key.DirectionRight -> {
                        onUserInteraction()
                        onSeek((positionMs + stepMs).coerceAtMost(durationMs))
                        true
                    }

                    else -> false
                }
            }
            .focusable()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .align(Alignment.Center)
                .background(
                    LocalContentColor.current.copy(alpha = 0.35f),
                    RoundedCornerShape(3.dp)
                )
        )

        Box(
            modifier = Modifier
                .fillMaxWidth(progress)
                .height(6.dp)
                .align(Alignment.CenterStart)
                .background(MaterialTheme.colorScheme.tvLiveContainer, RoundedCornerShape(3.dp))
        )
    }
}
@Composable
private fun ControlButton(
    icon: ImageVector,
    contentDescription: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.size(72.dp),
        border = ButtonDefaults.border(
            focusedBorder = Border(
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.tvFocusBorder)
            )
        ),
        contentPadding = PaddingValues(0.dp) // bỏ padding mặc định
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                modifier = Modifier.size(36.dp)
            )
        }
    }
}

private fun formatTime(timeMs: Long): String {
    val totalSeconds = (timeMs / 1000f).roundToLong().coerceAtLeast(0L)
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%d:%02d".format(minutes, seconds)
}
