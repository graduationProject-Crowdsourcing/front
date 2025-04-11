package project.graduation.crowd_sourcing.presentation.ui.navigation

import androidx.annotation.DrawableRes
import project.graduation.crowd_sourcing.presentation.R

sealed class Screen(val title: String, val route: String) {
    data object LoginScreen : Screen(title = "Login", route = "login")
    data object NotificationScreen : Screen(title = "알림", route = "notification")

    data object HistoryWorkScreen: Screen(title = "의뢰 기록", route = "history/work")
    data object HistoryRequestScreen: Screen(title = "작업 기록", route = "history/request")
    data object HistoryPointScreen: Screen(title = "포인트 내역", route = "history/point")

    data object AlarmSettingScreen: Screen(title = "알람 설정", route = "alarm")

    data object DetailStatsScreen: Screen(title = "세부 통계", route = "detail_stats")
    
    // 검색 관련 화면
    data object FilterSelectionScreen: Screen(title = "필터 선택", route = "filter_selection")
    data object SearchResultScreen: Screen(title = "검색 결과", route = "search_result")

    sealed class BottomScreen(
        val bTitle: String, val bRoute: String, @DrawableRes val icon: Int
    ) : Screen(title = bTitle, route = bRoute) {
        data object HomeScreen : BottomScreen(
            bTitle = "홈",
            bRoute = "home",
            icon = R.drawable.ic_home
        )

        data object SearchScreen : BottomScreen(
            bTitle = "검색",
            bRoute = "search",
            icon = R.drawable.ic_search
        )

        data object RequestScreen : BottomScreen(
            bTitle = "의뢰",
            bRoute = "reqeust",
            icon = R.drawable.ic_request
        )

        data object MyScreen : BottomScreen(
            bTitle = "마이 페이지",
            bRoute = "my",
            icon = R.drawable.ic_my
        )
    }

    companion object {
        fun fromRoute(route: String?): Screen {
            return when (route) {
                LoginScreen.route -> LoginScreen
                NotificationScreen.route -> NotificationScreen

                HistoryWorkScreen.route -> HistoryWorkScreen
                HistoryRequestScreen.route->HistoryRequestScreen
                HistoryPointScreen.route->HistoryPointScreen
                AlarmSettingScreen.route->AlarmSettingScreen

                DetailStatsScreen.route->DetailStatsScreen
                
                FilterSelectionScreen.route -> FilterSelectionScreen
                SearchResultScreen.route -> SearchResultScreen

                BottomScreen.HomeScreen.bRoute -> BottomScreen.HomeScreen
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