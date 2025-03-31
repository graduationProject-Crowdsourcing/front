package project.graduation.crowd_sourcing.presentation.ui.screen.notification

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import project.graduation.crowd_sourcing.presentation.R
import project.graduation.crowd_sourcing.presentation.ui.component.TopBar
import project.graduation.crowd_sourcing.presentation.ui.screen.base.BaseUiState
import project.graduation.crowd_sourcing.presentation.ui.navigation.Screen

// TODO: Domain Layer 구현 필요
// 1. Notification 도메인 모델 정의
//    - Notification 엔티티 정의
//    - NotificationType enum 정의
//    - NotificationMapper 구현
// 2. UseCase 구현
//    - GetNotificationsUseCase: 알림 목록 조회
//    - DeleteNotificationUseCase: 알림 삭제
//    - MarkNotificationAsReadUseCase: 알림 읽음 처리
// 3. Repository 구현
//    - NotificationRepository: 알림 데이터 처리
//    - Firebase Cloud Messaging 연동
// 4. UI 개선
//    - 알림 타입별 아이콘 매핑
//    - 빈 상태 UI 구현
//    - 알림 클릭 이벤트 처리

// 변경 내역:
// 1. 상태 관리 개선
//    - sealed class를 사용한 상태 관리에 맞춰 UI 로직 수정
//    - when 표현식을 사용한 상태별 UI 처리
// 2. 에러 처리 추가
//    - 에러 상태에 대한 UI 처리 추가
//    - 에러 메시지 표시
// 3. 로딩 상태 처리
//    - 로딩 상태에 대한 UI 처리 추가
//    - CircularProgressIndicator 표시
// 4. 컴포넌트 구조 개선
//    - 상태에 따른 조건부 렌더링
//    - 컴포넌트 간 일관된 스타일 적용
// 5. 새로고침 기능 개선
//    - 로딩 상태 처리 추가
//    - 에러 처리 추가

@Composable
fun NotificationView() {
    val viewModel: NotificationViewModel = hiltViewModel()
    val navController = rememberNavController()
    val uiState = viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "알림 내역",
            modifier = Modifier.padding(16.dp, top = 20.dp, bottom = 8.dp),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        when (val state = uiState.value) {
            is NotificationUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is NotificationUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = state.message)
                }
            }
            is NotificationUiState.Success -> {
                if (state.isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        items(state.notifications) { notification ->
                            NotificationItem(notification)
                            Divider(
                                modifier = Modifier.padding(vertical = 8.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

// TODO: Domain Layer 구현 필요
// - NotificationType을 도메인 모델로 이동
// - 알림 타입별 아이콘 매핑 로직을 UseCase로 분리
// - 현재는 Presentation Layer에서 직접 처리
@Composable
private fun NotificationItem(notification: NotificationItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(
                when (notification.type) {
                    NotificationType.SURVEY_COMPLETED -> R.drawable.ic_bell
                    NotificationType.NEW_SURVEY -> R.drawable.ic_bell
                    NotificationType.REWARD -> R.drawable.ic_bell
                }
            ),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        
        Spacer(modifier = Modifier.width(8.dp))
        
        Text(
            text = notification.message,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium
        )
    }
} 