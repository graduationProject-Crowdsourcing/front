package project.graduation.crowd_sourcing.presentation.ui.screen.notification

data class NotificationState(
    val notifications: List<NotificationItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

data class NotificationItem(
    val id: String,
    val message: String,
    val type: NotificationType,
    val timestamp: Long
)

enum class NotificationType {
    SURVEY_COMPLETED,
    NEW_SURVEY,
    REWARD
} 