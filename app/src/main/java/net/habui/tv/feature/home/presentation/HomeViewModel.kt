package net.habui.tv.feature.home.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import net.habui.tv.core.result.AppError
import net.habui.tv.core.result.Resource
import net.habui.tv.feature.home.domain.model.HomeContent
import net.habui.tv.feature.home.domain.model.Movie
import net.habui.tv.feature.home.domain.model.MovieSection
import net.habui.tv.feature.home.domain.usecase.GetHomeContentUseCase

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getHomeContentUseCase: GetHomeContentUseCase
) : ViewModel() {

    private val _uiState = MutableLiveData(HomeUiState(isLoading = true))
    val uiState: LiveData<HomeUiState> = _uiState

    init {
        loadHome()
    }

    fun onAction(action: HomeUiAction) {
        when (action) {
            HomeUiAction.OnRetryClick -> loadHome()
            is HomeUiAction.OnMovieClick -> Unit
        }
    }

    private fun loadHome() {
        viewModelScope.launch {
            getHomeContentUseCase().collect { resource ->
                when (resource) {
                    Resource.Loading -> {
                        _uiState.value = _uiState.value.orEmpty().copy(
                            isLoading = true,
                            error = null
                        )
                    }

                    is Resource.Success -> {
                        _uiState.value = resource.data.toUiState()
                    }

                    is Resource.Error -> {
                        val currentState = _uiState.value.orEmpty()

                        _uiState.value = currentState.copy(
                            isLoading = false,
                            error = resource.error.toUiError()
                        )
                    }
                }
            }
        }
    }

    private fun HomeContent.toUiState(): HomeUiState {
        return HomeUiState(
            featuredMovie = featuredMovie?.toUiModel(),
            sections = sections.map { it.toUiModel() },
            isLoading = false,
            error = null
        )
    }

    private fun MovieSection.toUiModel(): MovieSectionUiModel {
        return MovieSectionUiModel(
            title = title,
            movies = movies.map { it.toUiModel() }
        )
    }

    private fun Movie.toUiModel(): MovieUiModel {
        return MovieUiModel(
            id = id,
            title = title,
            imageUrl = imageUrl,
            videoUrl = videoUrl,
            playbackType = when (playbackType) {
                net.habui.tv.feature.home.domain.model.PlaybackType.Live -> PlaybackType.Live
                net.habui.tv.feature.home.domain.model.PlaybackType.Vod -> PlaybackType.Vod
            }
        )
    }

    private fun AppError.toUiError(): HomeUiError {
        return when (this) {
            AppError.Empty -> HomeUiError.Empty
            AppError.Timeout -> HomeUiError.Timeout
            AppError.Network -> HomeUiError.Network
            AppError.Unknown -> HomeUiError.Unknown
            is AppError.Api -> HomeUiError.Api(code)
        }
    }

    private fun HomeUiState?.orEmpty(): HomeUiState = this ?: HomeUiState()
}
