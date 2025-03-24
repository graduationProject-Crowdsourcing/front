package project.graduation.crowd_sourcing.presentation.ui.navigation

import androidx.annotation.DrawableRes
import project.graduation.crowd_sourcing.presentation.R

sealed class Screen(val title: String, val route: String) {
    data object LoginScreen : Screen(title = "Login", route = "login")

    sealed class BottomScreen(
        val bTitle: String, val bRoute: String, @DrawableRes val icon: Int
    ) : Screen(title = bTitle, route = bRoute){
        data object HomeScreen: BottomScreen(
            bTitle = "홈",
            bRoute = "home",
            icon = R.drawable.ic_home
        )

        data object SearchScreen: BottomScreen(
            bTitle = "검색",
            bRoute = "search",
            icon = R.drawable.ic_search
        )

        data object RequestScreen: BottomScreen(
            bTitle = "의뢰",
            bRoute = "reqeust",
            icon = R.drawable.ic_request
        )

        data object MyScreen: BottomScreen(
            bTitle = "마이 페이지",
            bRoute = "my",
            icon = R.drawable.ic_my
        )
    }

    companion object {
        fun fromRoute(route: String?): Screen {
            return when (route) {
                LoginScreen.route -> LoginScreen

                BottomScreen.HomeScreen.bRoute ->  BottomScreen.HomeScreen
                BottomScreen.SearchScreen.bRoute -> BottomScreen.SearchScreen
                BottomScreen.RequestScreen.bRoute -> BottomScreen.RequestScreen
                BottomScreen.MyScreen.bRoute -> BottomScreen.MyScreen
                else -> LoginScreen
            }
        }
    }
}

val screensInBottom = listOf(
    Screen.BottomScreen.HomeScreen,
    Screen.BottomScreen.SearchScreen,
    Screen.BottomScreen.RequestScreen,
    Screen.BottomScreen.MyScreen
)