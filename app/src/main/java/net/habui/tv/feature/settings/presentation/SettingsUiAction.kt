package net.habui.tv.feature.settings.presentation

sealed interface SettingsUiAction {
    data class ToggleAutoPlay(val enabled: Boolean) : SettingsUiAction
    data class ToggleSubtitle(val enabled: Boolean) : SettingsUiAction
    data class ChangeVideoQuality(val quality: String) : SettingsUiAction
    data class ChangeAudioLanguage(val language: String) : SettingsUiAction
    data class ChangeTheme(val theme: String) : SettingsUiAction
    data class ChangeFontScale(val scale: String) : SettingsUiAction
    data class ChangePrimaryColor(val color: String) : SettingsUiAction
    data class ToggleReducedAnimation(val enabled: Boolean) : SettingsUiAction
    object NavigateToPrivacyPolicy : SettingsUiAction
    object NavigateToTermsOfService : SettingsUiAction
}
