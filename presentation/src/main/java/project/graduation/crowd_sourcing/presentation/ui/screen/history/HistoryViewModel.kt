package project.graduation.crowd_sourcing.presentation.ui.screen.history

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HistoryViewModel:ViewModel(){
    private val _uiState = MutableStateFlow(HistoryUiState.initTest())
    val uiState = _uiState.asStateFlow()
}