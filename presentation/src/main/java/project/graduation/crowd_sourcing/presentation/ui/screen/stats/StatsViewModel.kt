package project.graduation.crowd_sourcing.presentation.ui.screen.stats

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import project.graduation.crowd_sourcing.domain.usecase.StatisticsUseCase
import project.graduation.crowd_sourcing.domain.usecase.WorkerUseCase
import javax.inject.Inject

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val statisticsUseCase: StatisticsUseCase,
    private val workerUseCase: WorkerUseCase
): ViewModel() {
    @RequiresApi(Build.VERSION_CODES.O)
    private val _uiState = MutableStateFlow(StatsUiState.test())
    @RequiresApi(Build.VERSION_CODES.O)
    val uiState = _uiState.asStateFlow()


    @RequiresApi(Build.VERSION_CODES.O)
    fun onSwitchType(){
        _uiState.update { prev->
            prev.copy(type = when(prev.type){
                StatsType.MART -> StatsType.PRODUCT
                StatsType.PRODUCT -> StatsType.MART
            })
        }
    }
}