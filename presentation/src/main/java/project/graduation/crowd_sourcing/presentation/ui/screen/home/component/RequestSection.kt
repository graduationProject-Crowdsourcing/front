package project.graduation.crowd_sourcing.presentation.ui.screen.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import project.graduation.crowd_sourcing.presentation.ui.screen.home.HomeUiState
import project.graduation.crowd_sourcing.presentation.ui.screen.home.HomeViewModel
import project.graduation.crowd_sourcing.presentation.ui.screen.home.Request

@Composable
fun RequestsSection(
    viewModel: HomeViewModel,
    state: HomeUiState.Success
) {
    // 현재 의뢰 목록
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 300.dp)
            .padding(top = 24.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.wrapContentSize()) {
            CurrentRequestsList(viewModel = viewModel, state = state)
        }
    }

    // 추천 의뢰 목록
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 300.dp)
            .padding(top = 24.dp),
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
    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "현재 의뢰",
            style = MaterialTheme.typography.titleMedium
        )
        IconButton(onClick = {}) {
            Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null)
        }
    }
    LazyColumn {
        items(state.requests.size) { index ->
            HomeListItem(request = state.requests[index])
            if (index < state.requests.size - 1) {
                Divider(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    color = MaterialTheme.colorScheme.outlineVariant,
                    thickness = 1.dp
                )
            }
        }
    }
}

@Composable
fun RecommendedRequestsList(
    viewModel: HomeViewModel,
    state: HomeUiState.Success
){
    Row(modifier = Modifier.fillMaxWidth()
        .padding(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "추천 의뢰",
            style = MaterialTheme.typography.titleMedium
        )
        IconButton(onClick = {}) {
            Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null)
        }
    }
    LazyColumn {
        items(state.requests.size) { index ->
            HomeListItem(request = state.requests[index])
            if (index < state.requests.size - 1) {
                Divider(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    color = MaterialTheme.colorScheme.outlineVariant,
                    thickness = 1.dp
                )
            }
        }
    }
}

@Composable
fun HomeListItem(request: Request){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, top = 16.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp) // 전체 원 크기
                .background(
                    color = Color(0xFFF0F0F0), // 연한 회색 배경
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Place, //핀 아이콘
                contentDescription = "위치 아이콘",
                tint = Color(0xFF4285F4),
                modifier = Modifier
                    .size(32.dp)
            )
        }
        Column(
            modifier = Modifier.weight(1f).padding(start = 16.dp)
        ) {
            Text(
                text = request.title,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = request.place,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Text(
            text = "리워드 : ${request.reward}p",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }

}