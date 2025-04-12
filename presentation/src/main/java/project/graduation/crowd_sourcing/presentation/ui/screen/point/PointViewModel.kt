package project.graduation.crowd_sourcing.presentation.ui.screen.point

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PointViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(PointUiState.test())
    val uiState = _uiState.asStateFlow()
}