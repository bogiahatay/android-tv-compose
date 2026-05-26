package net.habui.tv.feature.player.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import net.habui.tv.core.player.ExoPlayerFactory

@HiltViewModel
class PlayerViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val exoPlayerFactory: ExoPlayerFactory
) : ViewModel() {

    private val contentId: String = savedStateHandle["contentId"] ?: ""
    private val playbackType: String = savedStateHandle["playbackType"] ?: PlaybackMode.Vod.name
    private val title: String = savedStateHandle["title"] ?: ""
    private val streamUrl: String = savedStateHandle["streamUrl"] ?: ""

    private val playbackMode = if (playbackType.equals(PlaybackMode.Live.name, ignoreCase = true)) {
        PlaybackMode.Live
    } else {
        PlaybackMode.Vod
    }

    private val _uiState = MutableStateFlow(createInitialState())
    val uiState: StateFlow<PlayerUiState> = _uiState.asStateFlow()

    fun createPlayer() = exoPlayerFactory.create()

    fun onPlaybackChanged(
        isPlaying: Boolean,
        positionMs: Long,
        durationMs: Long
    ) {
        _uiState.update {
            it.copy(
                isPlaying = isPlaying,
                positionMs = positionMs.coerceAtLeast(0L),
                durationMs = durationMs.coerceAtLeast(0L)
            )
        }
    }

    private fun createInitialState(): PlayerUiState {
        return when (playbackMode) {
            PlaybackMode.Live -> PlayerUiState(
                contentId = contentId,
                playbackMode = PlaybackMode.Live,
                title = title.ifBlank { liveTitle(contentId) },
                programTitle = title.ifBlank { "Live TV" },
                programDescription = "",
                streamUrl = streamUrl.ifBlank { LIVE_STREAM_URL }
            )

            PlaybackMode.Vod -> PlayerUiState(
                contentId = contentId,
                playbackMode = PlaybackMode.Vod,
                title = title.ifBlank { vodTitle(contentId) },
                programTitle = "",
                programDescription = "",
                streamUrl = streamUrl.ifBlank { VOD_STREAM_URL }
            )
        }
    }

    private fun liveTitle(id: String): String {
        val index = id.substringAfter("live-", "0").toIntOrNull()?.plus(1) ?: 1
        return "Live $index"
    }

    private fun vodTitle(id: String): String {
        val index = id.substringAfter("vod-", "0").toIntOrNull() ?: 0
        return "VOD $index"
    }

    companion object {
        const val LIVE_STREAM_URL = "https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8"
//        const val VOD_STREAM_URL = "https://slave2.iblankdigital.net/tv/hls/nature/video.mp4"
        const val VOD_STREAM_URL = "https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8"
    }
}
