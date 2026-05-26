package net.habui.tv.feature.home.presentation

sealed interface HomeUiAction {
    data class OnMovieClick(val movieId: String) : HomeUiAction
    data object OnRetryClick : HomeUiAction
}
