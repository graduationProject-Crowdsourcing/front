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
//import com.google.android.gms.maps.model.CameraPosition
//import com.google.android.gms.maps.model.LatLng
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.google.maps.android.compose.*
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.rememberCameraPositionState
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.CircleOverlay
import com.naver.maps.map.compose.LocationTrackingMode
import project.graduation.crowd_sourcing.presentation.ui.screen.home.HomeUiState
import project.graduation.crowd_sourcing.presentation.ui.screen.home.Location
import project.graduation.crowd_sourcing.presentation.ui.screen.home.Request

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
                .padding(bottom = 8.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            if (isGoogleMapsAvailable) {
                NaverMapContent(state)
            } else {
                MapFallbackContent(state)
            }
        }
    }
}

//@Composable
//private fun GoogleMapContent(state: HomeUiState.Success) {
//    val currentLocation = state.currentLocation
//    val defaultLocation = LatLng(37.5665, 126.9780) // 서울시청 좌표를 기본값으로 사용
//
//    val currentLocationLatLng = currentLocation?.toLatLng() ?: defaultLocation
//
//    val cameraPositionState = rememberCameraPositionState {
//        position = CameraPosition.fromLatLngZoom(currentLocationLatLng, 12f)
//    }
//    val uiSettings by remember {
//        mutableStateOf(
//            MapUiSettings(
//                zoomControlsEnabled = true,
//                myLocationButtonEnabled = true,
//                mapToolbarEnabled = true
//            )
//        )
//    }
//    val mapProperties by remember {
//        mutableStateOf(
//            MapProperties(
//                isMyLocationEnabled = true,
//                mapType = MapType.NORMAL,
//                isBuildingEnabled = true,
//                isIndoorEnabled = true,
//                isTrafficEnabled = true
//            )
//        )
//    }
//
//    Box(modifier = Modifier.fillMaxSize()) {
//        GoogleMap(
//            modifier = Modifier.fillMaxSize(),
//            cameraPositionState = cameraPositionState,
//            properties = mapProperties,
//            uiSettings = uiSettings,
//            onMapLoaded = {
//                // 지도가 로드되면 현재 위치로 카메라 이동
//                cameraPositionState.position = CameraPosition.fromLatLngZoom(currentLocationLatLng, 15f)
//            }
//        ) {
//            if (currentLocation != null) {
//                // 현재 위치 마커
//                Marker(
//                    state = rememberMarkerState(position = currentLocationLatLng),
//                    title = "현재 위치",
//                    snippet = "위도: ${currentLocation.latitude}, 경도: ${currentLocation.longitude}"
//                )
//
//                // 검색 반경 원
//                Circle(
//                    center = currentLocationLatLng,
//                    clickable = false,
//                    fillColor = Color(0x220000FF),
//                    radius = state.searchRadius * 1000.0, // km를 미터로 변환
//                    strokeColor = Color(0xFF0000FF),
//                    strokeWidth = 2f,
//                    visible = true
//                )
//
//                // 의뢰 위치 마커들
//                state.requests.forEach { request ->
//                    Marker(
//                        state = rememberMarkerState(position = request.location.toLatLng()),
//                        title = request.title,
//                        snippet = "장소: ${request.place}, 보상: ${request.reward}원"
//                    )
//                }
//            }
//        }
//
//        if (currentLocation == null) {
//            Box(
//                modifier = Modifier.fillMaxSize(),
//                contentAlignment = Alignment.Center
//            ) {
//                Surface(
//                    modifier = Modifier
//                        .padding(16.dp)
//                        .wrapContentWidth(),
//                    color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.9f),
//                    shape = RoundedCornerShape(8.dp)
//                ) {
//                    Text(
//                        text = "위치 정보를 가져올 수 없습니다.",
//                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
//                        style = MaterialTheme.typography.bodyMedium,
//                        color = MaterialTheme.colorScheme.onErrorContainer,
//                        textAlign = TextAlign.Center
//                    )
//                }
//            }
//        }
//    }
//}

@OptIn(ExperimentalNaverMapApi::class)
@Composable
private fun NaverMapContent(state: HomeUiState.Success) {
    val currentLocation = state.currentLocation
    val defaultLocation = LatLng(37.5665, 126.9780) // 서울시청

    val currentLocationLatLng = currentLocation?.toLatLng() ?: defaultLocation

    val cameraPositionState : CameraPositionState = rememberCameraPositionState() {
        position = CameraPosition(currentLocationLatLng, 15.0)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        NaverMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(
                isBuildingLayerGroupEnabled = true,
                isTransitLayerGroupEnabled = false,
                locationTrackingMode = LocationTrackingMode.Follow
            ),
            uiSettings = MapUiSettings(
                isZoomControlEnabled = true,
                isLocationButtonEnabled = true,
                isCompassEnabled = true,
                isScaleBarEnabled = true
            ),
            onMapLoaded = {
                // 지도가 로드되면 현재 위치로 카메라 이동
                if (currentLocation != null) {
                    cameraPositionState.position = CameraPosition(currentLocationLatLng, 15.0)
                }
            }
        ) {
            if (currentLocation != null) {
                // 현재 위치 마커
                Marker(
                    state = MarkerState(position = currentLocationLatLng),
                    captionText = "현재 위치"
                )

                // 검색 반경 원
                CircleOverlay(
                    center = currentLocationLatLng,
                    radius = state.searchRadius * 1000.0, // km -> m
                    color = Color(0x220000FF),
                    outlineColor = Color(0xFF0000FF),
                    outlineWidth = 2.dp,
                    zIndex = 1
                )

                // 의뢰 위치 마커들
                state.requests.forEach { request ->
                    Marker(
                        state = MarkerState(position = request.location.toLatLng()),
                        captionText = request.title,
                        zIndex = 2
                    )
                }
            }
        }

        // 위치 정보 없을 때 경고 메시지
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