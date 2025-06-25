package project.graduation.crowd_sourcing.presentation.ui.screen.home.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import project.graduation.crowd_sourcing.presentation.ui.component.list.CommonList
import project.graduation.crowd_sourcing.presentation.ui.component.list.CommonListItemData
import project.graduation.crowd_sourcing.presentation.ui.screen.home.HomeUiState
import project.graduation.crowd_sourcing.presentation.ui.screen.home.HomeViewModel

/**
 * 전체 현재 의뢰 목록을 보여주는 화면
 */
@Composable
fun CurrentRequestsFullView(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState()
    
    // 현재 의뢰 목록 로드
    LaunchedEffect(Unit) {
        viewModel.loadCurrentRequests()
    }
    
    when (val state = uiState.value) {
        is HomeUiState.Loading -> {
            // 로딩 중일 때는 간단한 텍스트 표시
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(text = "로딩 중...")
            }
        }
        is HomeUiState.Error -> {
            // 에러 상태 표시
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "오류가 발생했습니다: ${state.message}",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
        is HomeUiState.Success -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                if (state.currentRequests.isEmpty()) {
                    Text(
                        text = "현재 진행 중인 의뢰가 없습니다.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    Text(
                        text = "총 ${state.currentRequests.size}개의 의뢰",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    CommonList(
                        list = state.currentRequests.map { request ->
                            CommonListItemData(
                                mainText = request.title,
                                subText = request.place,
                                leftText = "리워드 : ${request.reward}p",
                                onClick = {
                                    val commissionId = request.id.toInt()
                                    navController.navigate(
                                        project.graduation.crowd_sourcing.presentation.ui.navigation.Screen.AcceptRequestScreen.createRoute(commissionId)
                                    )
                                }
                            )
                        }
                    )
                }
            }
        }
    }
} 