package project.graduation.crowd_sourcing.presentation.ui.screen.stats

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class StatsViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(StatsUiState.test())
    val uiState = _uiState.asStateFlow()

}