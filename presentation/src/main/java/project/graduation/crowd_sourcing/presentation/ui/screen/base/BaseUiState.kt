package project.graduation.crowd_sourcing.presentation.ui.screen.base

import com.naver.maps.geometry.LatLng
import project.graduation.crowd_sourcing.presentation.ui.navigation.Screen

data class BaseUiState(
    val currentScreen: Screen,
    val currentLocation: LatLng,
    val serverLocation: LatLng,
    val isKaKaoLogined: Boolean = false
){
    companion object{
        fun init() = BaseUiState(
            currentScreen = Screen.BottomScreen.HomeScreen,
            currentLocation = LatLng(0.0, 0.0),
            serverLocation = LatLng(0.0, 0.0)
        )
    }
}