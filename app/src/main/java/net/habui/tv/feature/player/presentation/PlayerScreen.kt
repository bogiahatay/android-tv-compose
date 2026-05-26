package net.habui.tv.feature.player.presentation

import android.view.ViewGroup
import androidx.activity.compose.BackHandler
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView
import androidx.tv.material3.MaterialTheme
import kotlinx.coroutines.delay
import net.habui.tv.core.designsystem.tvPlayerBackground
import net.habui.tv.feature.player.components.LiveControls
import net.habui.tv.feature.player.components.PlayerOverlay
import net.habui.tv.feature.player.components.VodControls

@OptIn(UnstableApi::class)
@Composable
fun PlayerScreen(
    viewModel: PlayerViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val focusRequester = remember { FocusRequester() }
    val playPauseFocusRequester = remember { FocusRequester() }
    val liveActionFocusRequester = remember { FocusRequester() }
    var overlayVisible by remember { mutableStateOf(false) }
    var interactionNonce by remember { mutableStateOf(0) }
    var focusRestoreNonce by remember { mutableStateOf(0) }

    val player = remember { viewModel.createPlayer() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    LaunchedEffect(uiState.streamUrl, uiState.isLive) {
        player.setMediaItem(MediaItem.fromUri(uiState.streamUrl))
        player.prepare()
        player.playWhenReady = true
    }

    LaunchedEffect(player) {
        while (true) {
            viewModel.onPlaybackChanged(
                isPlaying = player.isPlaying,
                positionMs = player.currentPosition,
                durationMs = player.duration.takeIf { it > 0L } ?: 0L
            )
            delay(500L)
        }
    }

    LaunchedEffect(overlayVisible, interactionNonce) {
        if (overlayVisible) {
            delay(5_000L)
            overlayVisible = false
            focusRequester.requestFocus()
        }
    }

    LaunchedEffect(overlayVisible, focusRestoreNonce, uiState.isLive) {
        if (overlayVisible) {
            delay(50L)
            if (uiState.isLive) {
                focusRequester.requestFocus()
            } else {
                playPauseFocusRequester.requestFocus()
            }
        }
    }

    DisposableEffect(player) {
        onDispose {
            player.release()
        }
    }

    BackHandler(enabled = overlayVisible) {
        overlayVisible = false
        focusRequester.requestFocus()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.tvPlayerBackground)
            .focusRequester(focusRequester)
            .focusable()
            .onPreviewKeyEvent { event ->
                if (event.type != KeyEventType.KeyDown) {
                    return@onPreviewKeyEvent false
                }

                if (overlayVisible) {
                    interactionNonce++
                }

                when (event.key) {
                    Key.DirectionCenter,
                    Key.Enter,
                    Key.NumPadEnter -> {
                        if (!overlayVisible) {
                            overlayVisible = true
                            interactionNonce++
                            focusRestoreNonce++
                            true
                        } else {
                            false
                        }
                    }

                    Key.Back -> {
                        if (overlayVisible) {
                            overlayVisible = false
                            focusRequester.requestFocus()
                            true
                        } else {
                            false
                        }
                    }

                    else -> false
                }
            }
    ) {
        AndroidView(
            factory = {
                PlayerView(it).apply {
                    useController = false
                    this.player = player
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }
            },
            update = {
                it.player = player
            },
            modifier = Modifier.fillMaxSize()
        )

        PlayerOverlay(
            visible = overlayVisible,
            onFocusChanged = {}
        ) {
            if (uiState.isLive) {
                LiveControls(
                    uiState = uiState,
                    firstActionFocusRequester = liveActionFocusRequester,
                    onUserInteraction = {
                        interactionNonce++
                    },
                    modifier = Modifier.align(Alignment.BottomStart)
                )
            } else {
                VodControls(
                    uiState = uiState,
                    player = player,
                    playPauseFocusRequester = playPauseFocusRequester,
                    onUserInteraction = {
                        interactionNonce++
                    },
                    modifier = Modifier.align(Alignment.BottomStart)
                )
            }
        }
    }
}
