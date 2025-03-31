package project.graduation.crowd_sourcing.presentation.ui.screen.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// TODO: Domain Layer 구현 필요
// 1. UseCase 주입 및 사용
//    - ViewModel에 UseCase 주입
//    - 더미 데이터 대신 실제 UseCase 사용
// 2. 상태 관리 개선
//    - 에러 상태 추가
//    - 빈 상태 처리
//    - 새로고침 기능 구현
// 3. 알림 관리 기능
//    - 알림 삭제 기능
//    - 알림 읽음 처리
//    - 실시간 알림 업데이트

// 변경 내역:
// 1. 상태 관리 개선
//    - sealed class를 사용한 상태 관리 도입 (Loading, Success, Error)
//    - 상태 업데이트 시 when 표현식을 사용하여 타입 안전성 확보
// 2. 에러 처리 추가
//    - try-catch 블록을 사용한 예외 처리
//    - 에러 상태에 대한 UI 처리
// 3. 초기화 로직 개선
//    - loadInitialData() 함수를 통한 초기화
//    - viewModelScope를 사용한 코루틴 스코프 관리
// 4. 더미 데이터 관리
//    - companion object를 사용한 더미 데이터 관리
//    - 일관된 네이밍 컨벤션 적용
// 5. 새로고침 기능 개선
//    - 로딩 상태 처리 추가
//    - 에러 처리 추가

@HiltViewModel
class NotificationViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow<NotificationUiState>(NotificationUiState.Loading)
    val uiState: StateFlow<NotificationUiState> = _uiState.asStateFlow()

    companion object {
        private val DUMMY_NOTIFICATIONS = listOf(
            NotificationItem(
                id = "1",
                message = "'수색 아파트 - 벌기 편백' 요청이 승인되었습니다.",
                type = NotificationType.SURVEY_COMPLETED,
                timestamp = System.currentTimeMillis()
            ),
            NotificationItem(
                id = "2",
                message = "새로운 의뢰가 도착했습니다.",
                type = NotificationType.NEW_SURVEY,
                timestamp = System.currentTimeMillis() - 3600000
            ),
            NotificationItem(
                id = "3",
                message = "리워드: '상암 물품 - 벌기 가격'",
                type = NotificationType.REWARD,
                timestamp = System.currentTimeMillis() - 7200000
            )
        )
    }

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            try {
                _uiState.update { 
                    NotificationUiState.Success(
                        notifications = DUMMY_NOTIFICATIONS,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update { 
                    NotificationUiState.Error("알림을 불러오는데 실패했습니다: ${e.message}")
                }
            }
        }
    }

    fun refreshNotifications() {
        viewModelScope.launch {
            try {
                _uiState.update { currentState ->
                    when (currentState) {
                        is NotificationUiState.Success -> currentState.copy(isLoading = true)
                        else -> currentState
                    }
                }
                
                // TODO: Repository로 이동 필요
                _uiState.update { 
                    NotificationUiState.Success(
                        notifications = DUMMY_NOTIFICATIONS,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update { 
                    NotificationUiState.Error("알림을 새로고침하는데 실패했습니다: ${e.message}")
                }
            }
        }
    }
} 