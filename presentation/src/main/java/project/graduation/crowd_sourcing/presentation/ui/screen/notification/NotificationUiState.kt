package project.graduation.crowd_sourcing.presentation.ui.screen.notification

// TODO: Domain Layer 구현 필요
// - Notification 도메인 모델 정의
// - NotificationType enum 정의
// - 상태 관련 도메인 로직 분리

// 변경 내역:
// 1. 상태 클래스 분리
//    - NotificationViewModel에서 상태 클래스를 별도 파일로 분리
//    - 상태 관련 로직을 한 곳에서 관리

sealed class NotificationUiState {
    data class Success(
        val notifications: List<NotificationItem> = emptyList(),
        val isLoading: Boolean = false
    ) : NotificationUiState()
    data class Error(val message: String) : NotificationUiState()
    object Loading : NotificationUiState()

    data class NotificationItem(
        val id: String,
        val message: String,
        val type: NotificationType,
        val timestamp: Long
    )
}



enum class NotificationType {
    SURVEY_COMPLETED,
    NEW_SURVEY,
    REWARD
} 