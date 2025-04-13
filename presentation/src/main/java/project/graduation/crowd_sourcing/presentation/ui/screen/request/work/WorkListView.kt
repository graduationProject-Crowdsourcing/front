package project.graduation.crowd_sourcing.presentation.ui.screen.request.work

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import project.graduation.crowd_sourcing.presentation.ui.component.GrayDivider
import project.graduation.crowd_sourcing.presentation.ui.navigation.Screen
import project.graduation.crowd_sourcing.presentation.ui.screen.request.component.WorkListItem

// 작업 제출 버튼 클릭 시 등장 - 작업 리스트 페이지 뷰모델
@Composable
fun WorkListView(
    navController: NavController,
    viewModel: WorkListViewModel = viewModel()
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        Text(
            text = "현재 진행 중인 의뢰",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "제출할 작업을 선택해서 작업 내용을 작성하세요",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(viewModel.workList.size) { index ->
                val work = viewModel.workList[index]
                WorkListItem(work = work) {
                    navController.navigate("submit_work/${work.id}")
                }

                // 마지막 아이템 전까지만 Divider 추가
                if (index < viewModel.workList.size - 1) {
                    GrayDivider(modifier = Modifier.padding(horizontal = 16.dp))
                }
            }
        }
    }
}
