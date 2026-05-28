package net.habui.tv.feature.home.presentation

data class HomeUiState(
    val featuredMovies: List<FeaturedMovieUiModel> = emptyList(),
    val sections: List<MovieSectionUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: HomeUiError? = null
)

sealed interface HomeUiError {
    val message: String

    data object Empty : HomeUiError {
        override val message: String = "No content available."
    }

    data object Timeout : HomeUiError {
        override val message: String = "The request timed out. Please try again."
    }

    data object Network : HomeUiError {
        override val message: String = "Unable to connect. Please check your network."
    }

    data object Unknown : HomeUiError {
        override val message: String = "Something went wrong. Please try again."
    }

    data class Api(val code: Int) : HomeUiError {
        override val message: String = "Server error ($code). Please try again."
    }
}

data class FeaturedMovieUiModel(
    val id: String,
    val title: String,
    val description: String,
    val imageUrl: String,
    val videoUrl: String,
    val playbackType: PlaybackType = PlaybackType.Vod
)

fun FeaturedMovieUiModel.toMovieUiModel() = MovieUiModel(
    id = id,
    title = title,
    imageUrl = imageUrl,
    videoUrl = videoUrl,
    playbackType = playbackType
)

data class MovieSectionUiModel(
    val title: String,
    val movies: List<MovieUiModel>
)

data class MovieUiModel(
    val id: String,
    val title: String,
    val imageUrl: String,
    val videoUrl: String = "",
    val playbackType: PlaybackType = PlaybackType.Vod
)

enum class PlaybackType {
    Live,
    Vod
}
