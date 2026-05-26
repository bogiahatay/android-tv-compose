package net.habui.tv.feature.player.presentation

data class PlayerUiState(
    val contentId: String = "",
    val playbackMode: PlaybackMode = PlaybackMode.Vod,
    val title: String = "",
    val programTitle: String = "",
    val programDescription: String = "",
    val streamUrl: String = "",
    val isPlaying: Boolean = true,
    val positionMs: Long = 0L,
    val durationMs: Long = 0L
) {
    val isLive: Boolean
        get() = playbackMode == PlaybackMode.Live
}

enum class PlaybackMode {
    Live,
    Vod
}
