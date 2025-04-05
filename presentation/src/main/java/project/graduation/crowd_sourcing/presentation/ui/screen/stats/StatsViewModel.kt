package project.graduation.crowd_sourcing.presentation.ui.screen.stats

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class StatsViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(StatsUiState.test())
    val uiState = _uiState.asStateFlow()


    fun onSwitchType(){
        _uiState.update { prev->
            prev.copy(type = when(prev.type){
                StatsType.MART -> StatsType.PRODUCT
                StatsType.PRODUCT -> StatsType.MART
            })
        }
    }
}