package project.graduation.crowd_sourcing.presentation.ui.screen.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import project.graduation.crowd_sourcing.domain.usecase.HistoryUseCase
import project.graduation.crowd_sourcing.domain.usecase.RequesterUseCase
import project.graduation.crowd_sourcing.presentation.ui.screen.history.HistoryUiState.HistoryItem
import project.graduation.crowd_sourcing.presentation.ui.screen.history.HistoryUiState.StatsType
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val historyUseCase: HistoryUseCase,
    private val requesterUseCase: RequesterUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(HistoryUiState.init())
    val uiState = _uiState.asStateFlow()


    fun loadHistoryData(historyType: HistoryType) = viewModelScope.launch {
        when (historyType) {
            HistoryType.WORK -> {
                historyUseCase.getCommission().onSuccess { data ->
                    _uiState.update {
                        HistoryUiState(
                            stats = StatsType.Work.All(
                                totalWork = data.completed,
                                totalTime = data.countOrHour,
                                totalPoint = data.point
                            ) to StatsType.Work.Detail(
                                mostRegion = data.mostRegion.koreanName,
                                averageTime = data.countOrHour / data.completed,
                                mostCategory = (data.currentList + data.completedList)
                                    .map { it.category }
                                    .groupingBy { it }
                                    .eachCount()
                                    .maxByOrNull { it.value }
                                    ?.key ?: ""
                            ),
                            currentHistoryList = data.currentList.map {
                                HistoryItem(
                                    product = it.commission,
                                    category = it.category,
                                    date = it.commissionDate,
                                    point = it.commissionPoint,
                                    id = listOf(it.id),
                                    region = it.commissionRegion
                                )
                            },
                            totalHistoryList = (data.currentList + data.completedList).map {
                                HistoryItem(
                                    product = it.commission,
                                    category = it.category,
                                    date = it.commissionDate,
                                    point = it.commissionPoint,
                                    id = listOf(it.id),
                                    region = it.commissionRegion
                                )
                            }
                        )
                    }
                }.onFailure {
                    it.printStackTrace()
                }
            }

            HistoryType.REQUEST -> {
                historyUseCase.getRequest().onSuccess { data ->
                    _uiState.update {
                        HistoryUiState(
                            stats = StatsType.Request.All(
                                totalRequests = data.countOrHour,
                                totalPoint = data.point,
                                completedRequests = data.completed
                            ) to StatsType.Request.Detail(
                                mostRegion = data.mostRegion.koreanName,
                                mostCategory = data.mostCategory,
                                averagePoint = data.countOrHour / data.completed
                            ),
                            currentHistoryList = data.currentList.groupBy { it.commission }
                                .map { (product, items) ->
                                    HistoryItem(
                                        product = product,
                                        category = items.first().category,
                                        date = items.first().commissionDate,
                                        point = items.first().commissionPoint,
                                        id = items.map { it.id },
                                        region = items.first().commissionRegion
                                    )
                                },
                            totalHistoryList = (data.currentList + data.completedList)
                                .groupBy { it.commission }
                                .map { (product, items) ->
                                    HistoryItem(
                                        product = product,
                                        category = items.first().category,
                                        date = items.first().commissionDate,
                                        point = items.first().commissionPoint,
                                        id = items.map { it.id },
                                        region = items.first().commissionRegion
                                    )
                                }
                        )
                    }
                }.onFailure {
                    it.printStackTrace()
                }
            }
        }
    }
}