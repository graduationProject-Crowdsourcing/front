package project.graduation.crowd_sourcing.presentation.ui.screen.base

import android.annotation.SuppressLint
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.naver.maps.geometry.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import project.graduation.crowd_sourcing.presentation.ui.navigation.Screen

class BaseViewModel () : ViewModel() {
    private val _uiState = MutableStateFlow(BaseUiState.init())
    val uiState = _uiState.asStateFlow()

    fun updateCurrentScreen(navController: NavController) {
        val currentRoute = navController.currentBackStackEntry?.destination?.route
        currentRoute?.let { route ->
            val screen = Screen.fromRoute(route)
            _uiState.update { prev -> prev.copy(currentScreen = screen) }
        }
    }

    private val locationRequest = LocationRequest.Builder(
        Priority.PRIORITY_HIGH_ACCURACY, // 우선순위 + interval
        5_000L                           // 요청 간격(ms)
    ).apply {
        setMinUpdateIntervalMillis(5_000L) // 가장 빠른 요청 간격
    }.build()


    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            val newLocation = result.lastLocation ?: return

            _uiState.update { prev ->
                prev.copy(currentLocation = LatLng(newLocation.latitude, newLocation.longitude))
            }

            uiState.value.serverLocation.let { serverLoc ->
                val distanceResult = FloatArray(1) // 거리 결과를 담을 배열

                Location.distanceBetween(
                    serverLoc.latitude, serverLoc.longitude,
                    newLocation.latitude, newLocation.longitude,
                    distanceResult
                )

                if (distanceResult[0] >= 250f) {
                    onMoved250m(newLocation)
                }
            }


            Log.d("location", "${LatLng(newLocation.latitude, newLocation.longitude)}")
        }
    }

    @SuppressLint("MissingPermission")
    fun startTracking(fusedLocationClient: FusedLocationProviderClient) {
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    fun stopTracking(fusedLocationClient: FusedLocationProviderClient) {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun onMoved250m(location: Location) {
      _uiState.update { prev->
          prev.copy(serverLocation = LatLng(location.latitude, location.longitude))
      }
        Log.d("location", "Moved over 250m: ${location.latitude}, ${location.longitude}")
    }
}
