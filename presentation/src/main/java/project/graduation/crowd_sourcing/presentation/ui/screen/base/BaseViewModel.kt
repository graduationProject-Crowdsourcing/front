package project.graduation.crowd_sourcing.presentation.ui.screen.base

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import project.graduation.crowd_sourcing.presentation.ui.navigation.Screen

class BaseViewModel:ViewModel() {
    private val _uiState = MutableStateFlow(BaseUiState.init())
    val uiState = _uiState.asStateFlow()

    fun updateCurrentScreen(navController: NavController) {
        val currentRoute = navController.currentBackStackEntry?.destination?.route
        currentRoute?.let { route ->
            val screen = Screen.fromRoute(route) // 🔥 route를 기반으로 Screen을 찾는 함수
            _uiState.update { prev -> prev.copy(currentScreen = screen) }
        }
    }
}