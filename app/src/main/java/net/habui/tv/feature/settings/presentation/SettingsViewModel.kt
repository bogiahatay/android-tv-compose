package net.habui.tv.feature.settings.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableLiveData(SettingsUiState())
    val uiState: LiveData<SettingsUiState> = _uiState

    fun onAction(action: SettingsUiAction) {
        when (action) {
            is SettingsUiAction.ToggleAutoPlay -> {
                _uiState.value = _uiState.value?.copy(autoPlay = action.enabled)
            }
            is SettingsUiAction.ToggleSubtitle -> {
                _uiState.value = _uiState.value?.copy(subtitle = action.enabled)
            }
            is SettingsUiAction.ChangeVideoQuality -> {
                _uiState.value = _uiState.value?.copy(videoQuality = action.quality)
            }
            is SettingsUiAction.ChangeAudioLanguage -> {
                _uiState.value = _uiState.value?.copy(audioLanguage = action.language)
            }
            is SettingsUiAction.ChangeTheme -> {
                _uiState.value = _uiState.value?.copy(theme = action.theme)
            }
            is SettingsUiAction.ChangeFontScale -> {
                _uiState.value = _uiState.value?.copy(fontScale = action.scale)
            }
            is SettingsUiAction.ChangePrimaryColor -> {
                _uiState.value = _uiState.value?.copy(primaryColor = action.color)
            }
            is SettingsUiAction.ToggleReducedAnimation -> {
                _uiState.value = _uiState.value?.copy(reducedAnimation = action.enabled)
            }
            SettingsUiAction.NavigateToPrivacyPolicy -> {
                // Handle navigation
            }
            SettingsUiAction.NavigateToTermsOfService -> {
                // Handle navigation
            }
        }
    }
}
