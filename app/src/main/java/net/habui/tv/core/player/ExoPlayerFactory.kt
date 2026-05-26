package net.habui.tv.core.player

import android.content.Context
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.MediaSource
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ExoPlayerFactory @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val mediaSourceFactory: MediaSource.Factory
) {

    fun create(): ExoPlayer {
        return ExoPlayer.Builder(context, mediaSourceFactory)
            .build()
            .apply {
                repeatMode = Player.REPEAT_MODE_OFF
            }
    }
}
