package project.graduation.crowd_sourcing.presentation.ui.screen.point

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import project.graduation.crowd_sourcing.domain.usecase.HistoryUseCase
import javax.inject.Inject


@HiltViewModel
class PointViewModel @Inject constructor(
    private val historyUseCase: HistoryUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(PointUiState.test())
    val uiState = _uiState.asStateFlow()

    fun getHistoryData() = viewModelScope.launch {
        historyUseCase.getUsePointHistory().onSuccess { data ->
            _uiState.update { prev ->
                PointUiState(
                    list = data.map {
                        PointUiState.PointItem(
                            name = it.item,
                            date = it.date,
                            region = it.region.koreanName,
                            type = it.type,
                            point = it.point
                        )
                    }
                )
            }
        }.onFailure { }
    }
}