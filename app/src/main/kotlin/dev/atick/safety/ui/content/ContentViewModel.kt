package dev.atick.safety.ui.content

import dagger.hilt.android.lifecycle.HiltViewModel
import dev.atick.core.ui.base.BaseViewModel
import dev.atick.safety.ui.content.state.ContentUiState
import dev.atick.safety.ui.content.state.ScreenName
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ContentViewModel @Inject constructor() : BaseViewModel<ContentUiState>() {
    private val _contentUiState = MutableStateFlow(ContentUiState())
    val contentUiState: StateFlow<ContentUiState> = _contentUiState.asStateFlow()

    fun setCurrentScreen(currentScreen: ScreenName) {
        _contentUiState.update { it.copy(currentScreen = currentScreen) }
    }
}