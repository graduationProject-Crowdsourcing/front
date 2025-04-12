package project.graduation.crowd_sourcing.presentation.ui.screen.request.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.compose.*

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun MartLocationMapView(
    latitude: Double,
    longitude: Double,
    title: String
) {
    val martLatLng = LatLng(latitude, longitude)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition(martLatLng, 16.0) // 줌 레벨 16
    }

    Box(modifier = androidx.compose.ui.Modifier.fillMaxSize()) {
        NaverMap(
            cameraPositionState = cameraPositionState,
            properties = MapProperties(isBuildingLayerGroupEnabled = true),
            uiSettings = MapUiSettings()
        ) {
            Marker(
                state = MarkerState(position = martLatLng),
                captionText = title
            )
        }
    }
}
