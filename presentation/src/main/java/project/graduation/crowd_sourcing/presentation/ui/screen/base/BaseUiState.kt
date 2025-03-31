package project.graduation.crowd_sourcing.presentation.ui.screen.base

import project.graduation.crowd_sourcing.presentation.ui.navigation.Screen

data class BaseUiState(
    val currentScreen: Screen
){
    companion object{
        fun init() = BaseUiState(
            currentScreen = Screen.BottomScreen.HomeScreen,
        )
    }
}