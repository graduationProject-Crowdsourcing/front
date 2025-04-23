package project.graduation.crowd_sourcing.presentation.ui.screen.request.accept

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import project.graduation.crowd_sourcing.presentation.ui.component.ConfirmButton
import project.graduation.crowd_sourcing.presentation.ui.screen.request.component.WorkInfo
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import project.graduation.crowd_sourcing.presentation.ui.component.GrayDivider

// 의뢰 수락 페이지
@Composable
fun AcceptRequestView(
    navController: NavController,
    viewModel: AcceptRequestViewModel = viewModel()
) {
    val uiState = viewModel.uiState

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // 지도 (대체 이미지 or 네이버 지도)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Text("${uiState.place}", style = MaterialTheme.typography.bodyLarge)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("의뢰 정보", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        Column {
            WorkInfo(Icons.Default.Place, "위치", uiState.place)
            GrayDivider()
            WorkInfo(Icons.Default.Groups, "참여인원", uiState.participant)
            GrayDivider()
            WorkInfo(Icons.Default.Diamond, "리워드", "${uiState.reward}")
            GrayDivider()
            WorkInfo(Icons.Default.Edit, "의뢰 내역", uiState.title)
            GrayDivider()
            WorkInfo(Icons.Default.AccessTime, "의뢰 마감", uiState.deadline)
        }

        Spacer(modifier = Modifier.height(24.dp))

        ConfirmButton(
            text = "수락",
            onConfirm = {
                navController.navigate(
                    "acceptComplete/${uiState.place}/${uiState.title}/${uiState.reward}"
                )
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AcceptRequestViewPreview() {
    val acceptViewModel = object : AcceptRequestViewModel() {
        init {
            uiState = AcceptRequestUiState(
                id = "1",
                place = "상암 홈플러스",
                title = "딸기 한 박스",
                reward = 100,
                participant = "2/5",
                deadline = "2025/04/18 (금) 00:00"
            )
        }
    }

    AcceptRequestView(
        navController = rememberNavController(), // 더미 네비게이터
        viewModel = acceptViewModel
    )
}
