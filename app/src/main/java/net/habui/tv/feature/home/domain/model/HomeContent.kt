package net.habui.tv.feature.home.domain.model

data class HomeContent(
    val featuredMovies: List<FeaturedMovie> = emptyList(),
    val sections: List<MovieSection> = emptyList()
)

data class FeaturedMovie(
    val id: String,
    val title: String,
    val description: String,
    val imageUrl: String,
    val videoUrl: String,
    val playbackType: PlaybackType
)

data class MovieSection(
    val title: String,
    val movies: List<Movie>
)

data class Movie(
    val id: String,
    val title: String,
    val imageUrl: String,
    val videoUrl: String,
    val playbackType: PlaybackType
)

enum class PlaybackType {
    Live,
    Vod
}
