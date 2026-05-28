package net.habui.tv.feature.home.data.repository

import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import net.habui.tv.core.result.AppError
import net.habui.tv.core.result.Resource
import net.habui.tv.feature.home.data.remote.datasource.HomeRemoteDataSource
import net.habui.tv.feature.home.data.remote.dto.FeaturedMovieDto
import net.habui.tv.feature.home.data.remote.dto.HomeResponseDto
import net.habui.tv.feature.home.data.remote.dto.MovieDto
import net.habui.tv.core.network.ApiBaseUrl
import net.habui.tv.core.util.IoDispatcher
import net.habui.tv.feature.home.domain.model.FeaturedMovie
import net.habui.tv.feature.home.domain.model.HomeContent
import net.habui.tv.feature.home.domain.model.Movie
import net.habui.tv.feature.home.domain.model.MovieSection
import net.habui.tv.feature.home.domain.model.PlaybackType
import net.habui.tv.feature.home.domain.repository.HomeRepository

class HomeRepositoryImpl @Inject constructor(
    private val remoteDataSource: HomeRemoteDataSource,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @param:ApiBaseUrl private val baseUrl: String
) : HomeRepository {

    override suspend fun getHome(): Resource<HomeContent> = withContext(ioDispatcher) {
        try {
            val response = remoteDataSource.getHome()

            if (!response.isSuccessful) {
                return@withContext Resource.Error(AppError.Api(response.code()))
            }

            val content = response.body()?.toDomain()

            if (content == null || (content.featuredMovies.isEmpty() && content.sections.isEmpty())) {
                Resource.Error(AppError.Empty)
            } else {
                Resource.Success(content)
            }
        } catch (_: SocketTimeoutException) {
            Resource.Error(AppError.Timeout)
        } catch (_: UnknownHostException) {
            Resource.Error(AppError.Network)
        } catch (_: java.io.IOException) {
            Resource.Error(AppError.Network)
        } catch (_: Exception) {
            Resource.Error(AppError.Unknown)
        }
    }

    private fun HomeResponseDto.toDomain(): HomeContent {
        return HomeContent(
            featuredMovies = featuredMovies.orEmpty().mapNotNull { it.toDomain() },
            sections = sections.orEmpty()
                .mapIndexedNotNull { index, section ->
                    val playbackType = if (index == 0) PlaybackType.Live else PlaybackType.Vod
                    val movies = section.movies.orEmpty()
                        .mapNotNull { it.toDomain(playbackType) }

                    if (section.title.isNullOrBlank() || movies.isEmpty()) {
                        null
                    } else {
                        MovieSection(
                            title = section.title,
                            movies = movies
                        )
                    }
                }
        )
    }

    private fun FeaturedMovieDto.toDomain(): FeaturedMovie? {
        val safeId = id?.takeIf { it.isNotBlank() } ?: return null
        val safeTitle = title?.takeIf { it.isNotBlank() } ?: return null
        return FeaturedMovie(
            id = safeId,
            title = safeTitle,
            description = description.orEmpty(),
            imageUrl = imageUrl.toAbsoluteAssetUrl(),
            videoUrl = videoUrl.toAbsoluteAssetUrl(),
            playbackType = playbackType.toPlaybackType(PlaybackType.Vod)
        )
    }

    private fun MovieDto.toDomain(defaultPlaybackType: PlaybackType): Movie? {
        val safeId = id?.takeIf { it.isNotBlank() } ?: return null
        val safeTitle = title?.takeIf { it.isNotBlank() } ?: return null

        return Movie(
            id = safeId,
            title = safeTitle,
            imageUrl = imageUrl.toAbsoluteAssetUrl(),
            videoUrl = videoUrl.toAbsoluteAssetUrl(),
            playbackType = playbackType.toPlaybackType(defaultPlaybackType)
        )
    }

    private fun String?.toPlaybackType(defaultPlaybackType: PlaybackType): PlaybackType {
        return when {
            equals(PlaybackType.Live.name, ignoreCase = true) -> PlaybackType.Live
            equals(PlaybackType.Vod.name, ignoreCase = true) -> PlaybackType.Vod
            else -> defaultPlaybackType
        }
    }

    private fun String?.toAbsoluteAssetUrl(): String {
        val value = this?.trim().orEmpty()

        return when {
            value.startsWith("http://") || value.startsWith("https://") -> value
            value.startsWith("/") -> "$baseUrl${value.drop(1)}"
            value.isNotBlank() -> "$baseUrl$value"
            else -> ""
        }
    }
}
