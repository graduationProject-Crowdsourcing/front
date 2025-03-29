package project.graduation.crowd_sourcing.presentation.ui.screen.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor() : ViewModel() {
    
    private val _state = MutableStateFlow(NotificationState())
    val state: StateFlow<NotificationState> = _state.asStateFlow()

    init {
        loadNotifications()
    }

    private fun loadNotifications() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            
            // TODO: Replace with actual API call
            val dummyNotifications = listOf(
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

            _state.value = _state.value.copy(
                notifications = dummyNotifications,
                isLoading = false
            )
        }
    }

    fun refreshNotifications() {
        loadNotifications()
    }
} 