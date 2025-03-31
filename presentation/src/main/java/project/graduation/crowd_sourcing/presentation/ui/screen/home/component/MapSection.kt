package project.graduation.crowd_sourcing.presentation.ui.screen.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import project.graduation.crowd_sourcing.presentation.ui.screen.home.HomeUiState
import project.graduation.crowd_sourcing.presentation.ui.screen.home.Location
import project.graduation.crowd_sourcing.presentation.ui.screen.home.Request
import project.graduation.crowd_sourcing.presentation.utils.roundCommon
import project.graduation.crowd_sourcing.presentation.utils.spaceSmall

private fun Location.toLatLng(): LatLng = LatLng(latitude, longitude)

@Composable
fun MapSection(
    isGoogleMapsAvailable: Boolean,
    state: HomeUiState.Success
) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth()
    ) {
        val width = maxWidth
        Surface(
            modifier = Modifier
                .width(width)
                .height(width)
                .padding(bottom = spaceSmall()),
            shape = RoundedCornerShape(roundCommon())
        ) {
            if (isGoogleMapsAvailable) {
                GoogleMapContent(state)
            } else {
                MapFallbackContent(state)
            }
        }
    }
}

@Composable
private fun GoogleMapContent(state: HomeUiState.Success) {
    val currentLocation = state.currentLocation
    val defaultLocation = LatLng(37.5665, 126.9780) // 서울시청 좌표를 기본값으로 사용
    
    val currentLocationLatLng = currentLocation?.toLatLng() ?: defaultLocation
    
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(currentLocationLatLng, 12f)
    }
    val uiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                zoomControlsEnabled = false, // ➖➕ 버튼 비활성화
//                myLocationButtonEnabled = false, // 내 위치 버튼도 필요 없으면 끄기
            )
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(isMyLocationEnabled = false),
            uiSettings = uiSettings
        ) {
            if (currentLocation != null) {
                // 현재 위치 마커
                Marker(
                    state = rememberMarkerState(position = currentLocationLatLng),
                    title = "현재 위치"
                )

                // 검색 반경 원
                Circle(
                    center = currentLocationLatLng,
                    clickable = false,
                    fillColor = Color(0x220000FF),
                    radius = state.searchRadius * 1000.0, // km를 미터로 변환
                    strokeColor = Color(0xFF0000FF),
                    strokeWidth = 2f,
                    visible = true
                )

                // 의뢰 위치 마커들
                state.requests.forEach { request ->
                    Marker(
                        state = rememberMarkerState(position = request.location.toLatLng()),
                        title = request.title
                    )
                }
            }
        }

        if (currentLocation == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    modifier = Modifier
                        .padding(16.dp)
                        .wrapContentWidth(),
                    color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.9f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "위치 정보를 가져올 수 없습니다.",
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
private fun MapFallbackContent(state: HomeUiState.Success) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "지도를 불러올 수 없습니다.\n잠시 후 다시 시도해주세요.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "현재 위치: ${state.currentLocation!!.latitude}, ${state.currentLocation.longitude}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}