package net.habui.tv.navigation

import android.net.Uri

sealed class Route(val route: String) {
    object Home : Route("home")
    object Player : Route("player/{contentId}/{playbackType}/{title}/{streamUrl}") {
        fun createRoute(
            contentId: String,
            playbackType: String,
            title: String,
            streamUrl: String
        ): String {
            return "player/${Uri.encode(contentId)}/$playbackType/${Uri.encode(title)}/${Uri.encode(streamUrl)}"
        }
    }
    object Settings : Route("settings")
}
