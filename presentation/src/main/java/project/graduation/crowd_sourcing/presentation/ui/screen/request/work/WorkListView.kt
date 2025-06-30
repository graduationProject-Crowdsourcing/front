package project.graduation.crowd_sourcing.presentation.ui.screen.request.work

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import project.graduation.crowd_sourcing.presentation.ui.component.list.CommonList
import project.graduation.crowd_sourcing.presentation.ui.component.list.CommonListItemData
import project.graduation.crowd_sourcing.presentation.ui.navigation.Screen

// 작업 제출 버튼 클릭 시 등장 - 작업 리스트 페이지
@Composable
fun WorkListView(
    navController: NavController,
    viewModel: WorkListViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.getWorkList()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

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


        CommonList(
            list = viewModel.workList.map {
                CommonListItemData(
                    mainText = it.title,
                    subText = it.place.koreanName,
                    leftText = "리워드 : ${it.reward}p",
                    icon = null,
                    onClick = {
                        navController.navigate(Screen.SubmitWorkScreen.createRoute(it.id, martName = it.martName, category = it.category.koreanName))
                    }
                )
            }
        )
    }
}


