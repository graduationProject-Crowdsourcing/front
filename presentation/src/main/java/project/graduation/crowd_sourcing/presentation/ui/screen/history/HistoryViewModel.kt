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
import project.graduation.crowd_sourcing.domain.usecase.WorkerUseCase
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

    // 인터셉터에 의해 설정된 사용자 ID 사용 (테스트용)
    private val username = "didehddnjs89"


    fun loadHistoryData(historyType: HistoryType) {
        viewModelScope.launch {
            when (historyType) {
                HistoryType.WORK -> {
                    historyUseCase.getCommission().onSuccess { data ->
                        _uiState.update {
                            HistoryUiState(
                                stats = StatsType.Work.All(
                                    totalWork = data.completed,
                                    totalTime = data.hour,
                                    totalPoint = data.point
                                ) to StatsType.Work.Detail(
                                    mostRegion = data.mostRegion.name,
                                    averageTime = data.hour / data.completed,
                                    mostCategory = data.mostCategory.name
                                ),
                                currentHistoryList = data.currentList.map {
                                    HistoryItem(
                                        product = it.commission,
                                        category = it.commission,
                                        date = it.commissionDate,
                                        point = it.commissionPoint
                                    )
                                },
                                totalHistoryList = data.completedList.map {
                                    HistoryItem(
                                        product = it.commission,
                                        category = it.commission,
                                        date = it.commissionDate,
                                        point = it.commissionPoint
                                    )
                                }
                            )
                        }
                    }.onFailure {

                    }
                }

                HistoryType.REQUEST -> loadRequestHistoryData()
            }
        }
    }


    private suspend fun loadRequestHistoryData() {
        // Requester 관련 데이터 로딩
        try {
            // 전체 의뢰 횟수, 총 사용 포인트, 의뢰 성공 횟수 조회
            val requestStats = requesterUseCase.getRequestStats(username)
            val requestPoint = requesterUseCase.getRequestPoint(username)
            val requestSuccess = requesterUseCase.getRequestSuccess(username)

            // 가장 많이 의뢰한 지역 및 카테고리 조회
            val requestDetail = requesterUseCase.getRequestDetail(username)

            // 진행중인 의뢰 목록 조회
            val ongoingRequests = requesterUseCase.getOngoingRequests(username)

            // 완료된 의뢰 기록 조회
            val completedHistory = requesterUseCase.getRequestHistory(username, "COMPLETED")

            // 전체 통계 데이터 생성
            val allStats = StatsType.Request.All(
                totalRequests = requestStats.commissionCount,
                totalPoint = requestPoint.totalPoints,
                completedRequests = requestSuccess.successfulCommissions
            )

            // 상세 통계 데이터 생성
            val detailStats = StatsType.Request.Detail(
                mostRegion = requestDetail.mostRequestedRegion,
                averagePoint = if (requestStats.commissionCount > 0)
                    requestPoint.totalPoints / requestStats.commissionCount else 0,
                mostCategory = requestDetail.mostRequestedDayOfWeek // 현재 API에서는 카테고리 정보가 없어 요일 정보로 대체
            )

            // 현재 진행중인 의뢰 목록 변환
            val currentHistoryList = ongoingRequests.map { request ->
                HistoryItem(
                    product = request.commission,
                    category = request.commissionRegion, // 카테고리 정보가 없어 지역으로 대체
                    date = request.commissionDate,
                    point = request.commissionPoint
                )
            }

            // 완료된 의뢰 기록 변환
            val totalHistoryList = completedHistory.map { history ->
                HistoryItem(
                    product = history.commission,
                    category = history.category,
                    date = history.commissionDate,
                    point = history.commissionPoint
                )
            }

            // UI 상태 업데이트
            _uiState.update {
                HistoryUiState(
                    stats = allStats to detailStats,
                    currentHistoryList = currentHistoryList,
                    totalHistoryList = totalHistoryList
                )
            }
        } catch (e: Exception) {
            // 오류 발생 시 기본값 적용
            _uiState.update { HistoryUiState.init() }
        }
    }
}