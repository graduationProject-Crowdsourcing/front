package project.graduation.crowd_sourcing.presentation.ui.screen.stats

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import project.graduation.crowd_sourcing.domain.usecase.StatisticsUseCase
import javax.inject.Inject

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val useCase: StatisticsUseCase
): ViewModel() {
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