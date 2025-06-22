package project.graduation.crowd_sourcing.presentation.ui.screen.request.accept

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import project.graduation.crowd_sourcing.presentation.ui.component.ConfirmButton
import project.graduation.crowd_sourcing.presentation.ui.screen.request.component.WorkInfo
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import project.graduation.crowd_sourcing.presentation.ui.component.GrayDivider
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.compose.*

// 의뢰 수락 페이지
@Composable
fun AcceptRequestView(
    navController: NavController,
    requestId: String? = null, // 의뢰 ID 파라미터 추가
    viewModel: AcceptRequestViewModel = viewModel()
) {
    val uiState = viewModel.uiState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp) // 전체 패딩 증가
    ) {
        // 네이버 지도 (청량리 롯데마트 위치) - 지도 경계와 그림자 추가
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp) // 지도 높이 증가
                .padding(bottom = 24.dp), // 간격 증가
            shape = RoundedCornerShape(12.dp), // 모서리 둥글게
            shadowElevation = 4.dp, // 그림자 추가
            color = MaterialTheme.colorScheme.surface
        ) {
            AcceptRequestMapView(
                latitude = uiState.latitude,
                longitude = uiState.longitude,
                title = uiState.place
            )
        }

        // 의뢰 정보 제목 - 글자 크기와 굵기 증가
        Text(
            text = "의뢰 정보",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 16.dp) // 간격 증가
        )

        // 의뢰 정보 목록
        Column {
            AcceptRequestWorkInfo(Icons.Default.Place, "위치", uiState.place)
            GrayDivider()
            AcceptRequestWorkInfo(Icons.Default.Groups, "참여인원", uiState.participant)
            GrayDivider()
            AcceptRequestWorkInfo(Icons.Default.Diamond, "리워드", "${uiState.reward}")
            GrayDivider()
            AcceptRequestWorkInfo(Icons.Default.Edit, "의뢰 내역", uiState.title)
            GrayDivider()
            AcceptRequestWorkInfo(Icons.Default.AccessTime, "의뢰 마감", uiState.deadline)
        }

        Spacer(modifier = Modifier.height(8.dp)) // 간격 증가

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

// AcceptRequestView 전용 WorkInfo 컴포넌트 (SpaceBetween 레이아웃 적용)
@Composable
private fun AcceptRequestWorkInfo(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        
        // label과 value를 SpaceBetween으로 배치
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp
            )
        }
    }
}

// 의뢰 수락 페이지용 네이버 맵 컴포넌트
@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun AcceptRequestMapView(
    latitude: Double,
    longitude: Double,
    title: String
) {
    val martLatLng = LatLng(latitude, longitude)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition(martLatLng, 16.0) // 줌 레벨 16
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        NaverMap(
            modifier = Modifier
                .fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(isBuildingLayerGroupEnabled = true),
            uiSettings = MapUiSettings(
                isZoomControlEnabled = false,
                isLocationButtonEnabled = false,
                isCompassEnabled = false,
                isScaleBarEnabled = false
            )
        ) {
            Marker(
                state = MarkerState(position = martLatLng),
                captionText = title
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AcceptRequestViewPreview() {
    val acceptViewModel = object : AcceptRequestViewModel() {
        init {
            uiState = AcceptRequestUiState(
                id = "1",
                place = "청량리 롯데마트",
                title = "딸기 한 박스",
                reward = 100,
                participant = "2/5",
                deadline = "2025/04/18 (금) 00:00",
                latitude = 37.5818, // 청량리 롯데마트 위도
                longitude = 127.0368 // 청량리 롯데마트 경도
            )
        }
    }

    AcceptRequestView(
        navController = rememberNavController(),
        viewModel = acceptViewModel
    )
}
