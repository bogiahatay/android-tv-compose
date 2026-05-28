package net.habui.tv.feature.home.data.remote.dto

data class HomeResponseDto(
    val featuredMovies: List<FeaturedMovieDto>? = null,
    val sections: List<MovieSectionDto>? = null
)

data class FeaturedMovieDto(
    val id: String? = null,
    val title: String? = null,
    val description: String? = null,
    val imageUrl: String? = null,
    val videoUrl: String? = null,
    val playbackType: String? = null
)

data class MovieSectionDto(
    val title: String? = null,
    val movies: List<MovieDto>? = null
)

data class MovieDto(
    val id: String? = null,
    val title: String? = null,
    val imageUrl: String? = null,
    val videoUrl: String? = null,
    val playbackType: String? = null
)
