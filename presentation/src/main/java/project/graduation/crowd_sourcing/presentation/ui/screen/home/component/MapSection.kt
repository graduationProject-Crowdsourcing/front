package project.graduation.crowd_sourcing.presentation.ui.screen.home.component

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.CircleOverlay
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.LocationTrackingMode
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.rememberCameraPositionState
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.MarkerIcons
import project.graduation.crowd_sourcing.domain.model.entity.martsearch.MartEntity
import project.graduation.crowd_sourcing.presentation.ui.screen.home.HomeUiState
import project.graduation.crowd_sourcing.presentation.ui.screen.home.Location
import project.graduation.crowd_sourcing.presentation.ui.screen.home.Request

/** 위치 정보를 LatLng 객체로 변환하는 확장 함수 */
fun Location.toLatLng(): LatLng = LatLng(latitude, longitude)

/**
 * 지도 관련 상수 값 정의
 * 
 * - DEFAULT_ZOOM: 기본 확대/축소 레벨
 * - DEFAULT_LOCATION: 기본 위치 (서울시청)
 * - CIRCLE_FILL_COLOR: 반경 원 내부 색상
 * - CIRCLE_OUTLINE_COLOR: 반경 원 외곽선 색상
 * - CIRCLE_OUTLINE_WIDTH: 반경 원 외곽선 두께
 * - MARKER_ZINDEX: 마커 Z-인덱스
 * - CIRCLE_ZINDEX: 원 Z-인덱스
 * - METERS_PER_KM: KM당 미터 변환 상수
 * - USER_LOCATION_MARKER_COLOR: 사용자 위치 마커 색상
 * - MART_WITH_COMMISSION_COLOR: 의뢰가 있는 마트 마커 색상
 * - MART_WITHOUT_COMMISSION_COLOR: 의뢰가 없는 마트 마커 색상
 */
private object MapConstants {
    const val DEFAULT_ZOOM = 15.0
    val DEFAULT_LOCATION = LatLng(37.5665, 126.9780) // 서울시청
    val CIRCLE_FILL_COLOR = Color(0x220000FF)
    val CIRCLE_OUTLINE_COLOR = Color(0xFF0000FF)
    val CIRCLE_OUTLINE_WIDTH = 2.dp
    const val MARKER_ZINDEX = 2
    const val CIRCLE_ZINDEX = 1
    const val METERS_PER_KM = 1000.0
    val USER_LOCATION_MARKER_COLOR = Color(0xFF4285F4) // 구글맵 스타일 파란색
    val MART_WITH_COMMISSION_COLOR = Color(0xFF34A853) // 구글맵 스타일 초록색
    val MART_WITHOUT_COMMISSION_COLOR = Color(0xFF9AA0A6) // 연한 회색
}

/**
 * 홈 화면의 지도 섹션 컴포넌트
 * 
 * @param isMapServiceAvailable 지도 서비스 사용 가능 여부
 * @param state 홈 화면 상태
 * @param onMartClick 마트 클릭 이벤트 핸들러
 */
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun MapSection(
    isMapServiceAvailable: Boolean,
    state: HomeUiState.Success,
    onMartClick: (MartEntity) -> Unit = {}
) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth()
    ) {
        val mapWidth = maxWidth
        Surface(
            modifier = Modifier
                .width(mapWidth)
                .height(mapWidth)
                .padding(bottom = 8.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            if (isMapServiceAvailable) {
                NaverMapView(state, onMartClick)
            } else {
                MapFallbackContent(state)
            }
        }
    }
}

/**
 * 네이버 맵 컴포넌트
 * 
 * @param state 홈 화면 상태
 * @param onMartClick 마트 클릭 이벤트 핸들러
 */
@OptIn(ExperimentalNaverMapApi::class)
@Composable
private fun NaverMapView(
    state: HomeUiState.Success,
    onMartClick: (MartEntity) -> Unit
) {
    val currentLocation = state.currentLocation
    val defaultLocation = MapConstants.DEFAULT_LOCATION

    // 사용자 위치 또는 기본 위치
    val mapCenterLocation = currentLocation?.toLatLng() ?: defaultLocation

    // 카메라 위치 상태
    val cameraPositionState: CameraPositionState = rememberCameraPositionState {
        position = CameraPosition(mapCenterLocation, MapConstants.DEFAULT_ZOOM)
    }

    // 명시적인 메모리 관리를 위한 DisposableEffect
    DisposableEffect(key1 = currentLocation) {
        onDispose {
            // 리소스 명시적 정리
            cameraPositionState.position = CameraPosition(mapCenterLocation, 0.0)
        }
    }


    Box(modifier = Modifier
        .fillMaxSize()
    ) {
        NaverMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = createMapProperties(),
            uiSettings = createMapUiSettings(),
            onMapLoaded = {
                // 지도 로드 완료 시 현재 위치로 카메라 이동
                if (currentLocation != null) {
                    cameraPositionState.position = CameraPosition(
                        mapCenterLocation, 
                        MapConstants.DEFAULT_ZOOM
                    )
                }
            }
        ) {
            if (currentLocation != null) {
                // 현재 위치 LatLng 캐싱
                val currentLatLng = currentLocation.toLatLng()
                
                // 사용자 위치 마커
                DrawUserLocationMarker(currentLocation)
                
                // 검색 반경 원
                DrawSearchRadiusCircle(
                    center = currentLatLng,
                    radiusInKm = state.searchRadius
                )

                // 주변 마트 마커들
                DrawMartMarkers(
                    martEntities = state.nearbyMartEntities,
                    onMartClick = onMartClick
                )

                // 의뢰 위치 마커들 (현재 작업중인 의뢰 + 추천 의뢰)
                // DrawRequestMarkers(state.currentRequests + state.recommendedRequests)
            }
        }

        // 위치 정보 없을 때 경고 메시지
        if (currentLocation == null) {
            ShowLocationErrorMessage()
        }
    }
}

/**
 * 지도 속성 생성
 */
@Composable
private fun createMapProperties() = MapProperties(
    isBuildingLayerGroupEnabled = true,
    isTransitLayerGroupEnabled = false,
    locationTrackingMode = LocationTrackingMode.Follow
)

/**
 * 지도 UI 설정 생성
 */
@Composable
private fun createMapUiSettings() = MapUiSettings(
    isZoomControlEnabled = true,
    isLocationButtonEnabled = true,
    isCompassEnabled = true,
    isScaleBarEnabled = true
)

/**
 * 사용자 위치 마커 표시
 */
@OptIn(ExperimentalNaverMapApi::class)
@Composable
private fun DrawUserLocationMarker(location: Location) {
    Marker(
        state = MarkerState(position = location.toLatLng()),
        captionText = "현재 위치",
        icon = MarkerIcons.BLACK,
        iconTintColor = MapConstants.USER_LOCATION_MARKER_COLOR
    )
}

/**
 * 검색 반경 원 표시
 */
@Composable
private fun DrawSearchRadiusCircle(center: LatLng, radiusInKm: Float) {
    CircleOverlay(
        center = center,
        radius = radiusInKm * MapConstants.METERS_PER_KM, // km -> m
        color = MapConstants.CIRCLE_FILL_COLOR,
        outlineColor = MapConstants.CIRCLE_OUTLINE_COLOR,
        outlineWidth = MapConstants.CIRCLE_OUTLINE_WIDTH,
        zIndex = MapConstants.CIRCLE_ZINDEX
    )
}

/**
 * 주변 마트 마커들 표시
 */
@OptIn(ExperimentalNaverMapApi::class)
@Composable
private fun DrawMartMarkers(
    martEntities: List<MartEntity>,
    onMartClick: (MartEntity) -> Unit
) {
    martEntities.forEach { mart ->
        if (mart.existCommission == 1) {
            // 의뢰가 있는 마트: 기본 마커
            Marker(
                state = MarkerState(position = LatLng(mart.latitude, mart.longitude)),
                captionText = mart.martName,
                zIndex = MapConstants.MARKER_ZINDEX,
                onClick = { 
                    onMartClick(mart)
                    true // 클릭 이벤트 소모
                }
            )
        } else {
            // 의뢰가 없는 마트: 검은색 마커 + 회색 색상 적용
            Marker(
                state = MarkerState(position = LatLng(mart.latitude, mart.longitude)),
                captionText = mart.martName,
                zIndex = MapConstants.MARKER_ZINDEX,
                icon = MarkerIcons.BLACK,
                iconTintColor = MapConstants.MART_WITHOUT_COMMISSION_COLOR,
                onClick = { 
                    onMartClick(mart)
                    true // 클릭 이벤트 소모
                }
            )
        }
    }
}

/**
 * 의뢰 위치 마커들 표시
 */
@OptIn(ExperimentalNaverMapApi::class)
@Composable
private fun DrawRequestMarkers(requests: List<Request>) {
    requests.forEach { request ->
        Marker(
            state = MarkerState(position = request.location.toLatLng()),
            captionText = request.title,
            zIndex = MapConstants.MARKER_ZINDEX
        )
    }
}

/**
 * 위치 정보 오류 메시지 표시
 */
@Composable
private fun ShowLocationErrorMessage() {
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

/**
 * 지도 대체 콘텐츠 (지도 서비스 불가능 시)
 */
@Composable
private fun MapFallbackContent(state: HomeUiState.Success) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "지도 서비스를 사용할 수 없습니다.",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}