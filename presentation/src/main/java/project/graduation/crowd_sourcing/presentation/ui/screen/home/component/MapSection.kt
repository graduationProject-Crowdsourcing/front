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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import project.graduation.crowd_sourcing.presentation.ui.screen.home.HomeUiState
import project.graduation.crowd_sourcing.presentation.ui.screen.home.toLatLng

@Composable
fun MapSection(isGoogleMapsAvailable: Boolean, uiState: State<HomeUiState>) {
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
                GoogleMapContent(uiState)
            } else {
                MapFallbackContent(uiState)
            }
        }
    }
}

@Composable
private fun GoogleMapContent(uiState: State<HomeUiState>) {
    val seoul = LatLng(37.5665, 126.9780)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(seoul, 10f)
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(isMyLocationEnabled = false)
    ) {
        // 현재 위치 마커
        val currentLocationState = rememberMarkerState(
            position = uiState.value.currentLocation.toLatLng()
        )
        Marker(state = currentLocationState, title = "현재 위치")

        // 의뢰 위치 마커들
        uiState.value.requests.forEach { request ->
            val markerState = rememberMarkerState(
                position = request.location.toLatLng()
            )
            Marker(state = markerState, title = request.title)
        }
    }
}

@Composable
private fun MapFallbackContent(uiState: State<HomeUiState>) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "지도를 불러올 수 없습니다.\n잠시 후 다시 시도해주세요.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "현재 위치: ${uiState.value.currentLocation.latitude}, ${uiState.value.currentLocation.longitude}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}