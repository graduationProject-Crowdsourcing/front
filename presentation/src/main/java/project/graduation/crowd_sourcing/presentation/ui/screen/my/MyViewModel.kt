package project.graduation.crowd_sourcing.presentation.ui.screen.my

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MyViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(MyUiState.init())
    val uiState = _uiState.asStateFlow()
}