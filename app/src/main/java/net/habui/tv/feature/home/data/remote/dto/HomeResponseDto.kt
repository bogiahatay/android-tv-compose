package net.habui.tv.feature.home.data.remote.dto

data class HomeResponseDto(
    val featuredMovie: MovieDto? = null,
    val sections: List<MovieSectionDto>? = null
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
