package project.graduation.crowd_sourcing.presentation.ui.screen.stats

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import project.graduation.crowd_sourcing.domain.model.Category
import project.graduation.crowd_sourcing.domain.model.Region
import project.graduation.crowd_sourcing.domain.usecase.StatisticsUseCase
import project.graduation.crowd_sourcing.domain.usecase.WorkerUseCase
import javax.inject.Inject

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val statisticsUseCase: StatisticsUseCase,
    private val workerUseCase: WorkerUseCase
) : ViewModel() {
    @RequiresApi(Build.VERSION_CODES.O)
    private val _uiState = MutableStateFlow(StatsUiState.test())

    @RequiresApi(Build.VERSION_CODES.O)
    val uiState = _uiState.asStateFlow()


    @RequiresApi(Build.VERSION_CODES.O)
    fun getDetail(id: Int) = viewModelScope.launch {
        statisticsUseCase.getDetail(id).onSuccess {
            _uiState.update { prev ->
                prev.copy(
                    requestRegion = it.commissionregion,
                    requestStartDate = it.commissionDate,
                    requestCompleteDate = it.expirationDate,
                    requestProduct = it.category
                )
            }
        }.onFailure {
            it.printStackTrace()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getDataList(id: Int, category: String) = viewModelScope.launch {
        when (uiState.value.type) {
            StatsType.MART -> {
                statisticsUseCase.getMart(
                    id, category
                ).onSuccess { dataList ->
                    _uiState.update { prev ->
                        prev.copy(
                            dataList = dataList.map {
                                StatsUiState.StatsListItem(name = it.mart, price = it.categoryPrice)
                            }
                        )
                    }
                }.onFailure {
                    it.printStackTrace()
                }
            }

            StatsType.PRODUCT -> {
                statisticsUseCase.getItem(
                    id, category
                ).onSuccess { dataList ->
                    _uiState.update { prev ->
                        prev.copy(
                            dataList = dataList.map {
                                StatsUiState.StatsListItem(name = it.item, price = it.averagePrice)
                            }
                        )
                    }
                }.onFailure {
                    it.printStackTrace()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onSwitchType() {
        _uiState.update { prev ->
            prev.copy(
                type = when (prev.type) {
                    StatsType.MART -> StatsType.PRODUCT
                    StatsType.PRODUCT -> StatsType.MART
                }
            )
        }
    }
}