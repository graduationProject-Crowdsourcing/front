package project.graduation.crowd_sourcing.presentation.ui.screen.request.accept

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import project.graduation.crowd_sourcing.presentation.ui.component.ConfirmButton
import project.graduation.crowd_sourcing.presentation.ui.component.GrayDivider
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.compose.*

// 의뢰 수락 페이지
@OptIn(ExperimentalNaverMapApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AcceptRequestView(
    navController: NavController,
    commissionId: Int,
    viewModel: AcceptRequestViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    // commissionId가 변경될 때마다 데이터를 로드
    LaunchedEffect(commissionId) {
        viewModel.loadCommissionDetail(commissionId)
    }

    // 로딩 상태 처리
    if (uiState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    // 에러 상태 처리
    uiState.errorMessage?.let { errorMessage ->
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = errorMessage,
                color = Color.Red,
                modifier = Modifier.padding(24.dp)
            )
        }
        return
    }

    // 수락 성공 시 자동으로 완료 페이지로 이동
    LaunchedEffect(uiState.isAcceptSuccess) {
        if (uiState.isAcceptSuccess) {
            navController.navigate(
                "acceptComplete/${uiState.martName}/${uiState.commission}/${uiState.commissionPoint}"
            )
        }
    }

    // 수락 실패 다이얼로그
    uiState.acceptErrorMessage?.let { errorMessage ->
        AlertDialog(
            onDismissRequest = { viewModel.clearAcceptError() },
            title = { Text("수락 실패") },
            text = { Text(errorMessage) },
            confirmButton = {
                TextButton(
                    onClick = { viewModel.clearAcceptError() }
                ) {
                    Text("확인")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // 네이버맵을 사용한 지도 섹션
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(screenHeight * 0.25f)
                .shadow(2.dp, RoundedCornerShape(8.dp)),
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            val position = LatLng(uiState.latitude, uiState.longitude)
            val cameraPositionState = rememberCameraPositionState {
                this.position = CameraPosition(position, 15.0)
            }

            // 위도/경도가 변경될 때 카메라를 해당 위치로 이동
            LaunchedEffect(uiState.latitude, uiState.longitude) {
                val newPosition = LatLng(uiState.latitude, uiState.longitude)
                cameraPositionState.animate(
                    update = CameraUpdate.scrollTo(newPosition),
                    durationMs = 800
                )
            }

            NaverMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState
            ) {
                Marker(
                    state = MarkerState(position = position),
                    captionText = uiState.martName
                )
            }
        }

        // 의뢰 정보 제목
        Text(
            text = "의뢰 정보",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // 의뢰 정보 목록
        Column {
            AcceptRequestWorkInfo(Icons.Default.Place, "위치", "${uiState.region} ${uiState.martName}")
            GrayDivider()
            AcceptRequestWorkInfo(Icons.Default.Category, "카테고리", uiState.category)
            GrayDivider()
            AcceptRequestWorkInfo(Icons.Default.ShoppingCart, "상품", uiState.item)
            GrayDivider()
            AcceptRequestWorkInfo(Icons.Default.Groups, "참여인원", "${uiState.commissionCount}명")
            GrayDivider()
            AcceptRequestWorkInfo(Icons.Default.Diamond, "리워드", "${uiState.commissionPoint}P")
            GrayDivider()
            AcceptRequestWorkInfo(Icons.Default.Edit, "의뢰 내역", uiState.commission)
            GrayDivider()
            AcceptRequestWorkInfo(Icons.Default.AccessTime, "의뢰 마감", uiState.expirationDate)
        }

        Spacer(modifier = Modifier.height(24.dp))

        ConfirmButton(
            text = if (uiState.isAcceptLoading) "처리 중..." else "수락",
            onConfirm = {
                if (!uiState.isAcceptLoading) {
                    viewModel.acceptRequest()
                }
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
