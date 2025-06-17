package project.graduation.crowd_sourcing.presentation.ui.screen.home.component

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import project.graduation.crowd_sourcing.presentation.R
import project.graduation.crowd_sourcing.presentation.ui.component.list.CommonList
import project.graduation.crowd_sourcing.presentation.ui.component.list.CommonListItemData
import project.graduation.crowd_sourcing.presentation.ui.screen.home.HomeUiState
import project.graduation.crowd_sourcing.presentation.ui.screen.home.HomeViewModel

@Composable
fun RequestsSection(
    viewModel: HomeViewModel,
    state: HomeUiState.Success
) {
    // 현재 의뢰 목록
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 300.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.wrapContentSize()) {
            CurrentRequestsList(viewModel = viewModel, state = state)
        }
    }

    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.space_medium)))

    // 추천 의뢰 목록
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 300.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.wrapContentSize()) {
            RecommendedRequestsList(viewModel = viewModel, state = state)
        }
    }
}

@Composable
fun CurrentRequestsList(
    viewModel: HomeViewModel,
    state: HomeUiState.Success
) {
    // 현재 작업중인 의뢰 목록을 로드하는 LaunchedEffect
    LaunchedEffect(Unit) {
        viewModel.loadCurrentRequests()
    }
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "현재 의뢰",
            style = MaterialTheme.typography.titleMedium
        )
//        IconButton(onClick = {}) {
//            Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null)
//        }
    }
    CommonList(
        list = state.currentRequests.map {
            CommonListItemData(
                mainText = it.title,
                subText = it.place,
                leftText = "리워드 : ${it.reward}p",
                onClick = {}
            )
        }
    )
}

@Composable
fun RecommendedRequestsList(
    viewModel: HomeViewModel,
    state: HomeUiState.Success
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "추천 의뢰",
            style = MaterialTheme.typography.titleMedium
        )
//        IconButton(onClick = {}) {
//            Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null)
//        }
    }
    CommonList(
        list = state.recommendedRequests.map {
            CommonListItemData(
                mainText = it.title,
                subText = it.place,
                leftText = "리워드 : ${it.reward}p",
                onClick = {}
            )
        }
    )
}

@SuppressLint("StateFlowValueCalledInComposition")
@Preview
@Composable
fun RequestsSectionPrev() {
    val viewModel: HomeViewModel = viewModel()
    RequestsSection(
        viewModel = viewModel,
        state = (viewModel.uiState.value as HomeUiState.Success)
    )
}