package project.graduation.crowd_sourcing.presentation.ui.navigation

import androidx.annotation.DrawableRes
import project.graduation.crowd_sourcing.domain.model.Category
import project.graduation.crowd_sourcing.domain.model.Region
import project.graduation.crowd_sourcing.presentation.R

sealed class Screen(val title: String, val route: String) {
    data object LoginScreen : Screen(title = "Login", route = "login")
    data object LogoutConfirmScreen : Screen(title = "로그아웃 확인", route = "logout_confirm")

    data object NotificationScreen : Screen(title = "알림", route = "notification")

    // 의뢰 작성 관련 화면
    data object RequestFormScreen : Screen(title = "의뢰 작성", route = "request_form")
    data object RequestCompleteScreen : Screen(title = "의뢰 완료", route = "request_complete")

    // 의뢰 수락 관련 화면
    data object AcceptRequestScreen : Screen(title = "의뢰 수락", route = "accept_request/{commissionId}") {
        const val routeWithArg = "accept_request/{commissionId}"
        fun createRoute(commissionId: Int): String = "accept_request/$commissionId"
    }
    data object AcceptCompleteScreen : Screen(title = "의뢰 수락 완료", route = "acceptComplete/{place}/{title}/{reward}") {
        fun createRoute(place: String, title: String): String =
            "acceptComplete/$place/$title"
    }

    // 작업 제출 관련 화면
    data object WorkListScreen : Screen(title = "작업 리스트", route = "work_list")
    data object SubmitWorkScreen : Screen(title = "작업 제출", route = "submit_work/{workId}/{category}/{martName}") {
        const val routeWithArg = "submit_work/{workId}/{category}/{martName}"

        fun createRoute(workId: Int, category: String, martName: String): String = "submit_work/$workId/$category/$martName"
    }
    data object WorkCompleteScreen : Screen(title = "작업 완료", route = "work_complete") {
        const val routeWithArgs = "work_complete/{place}/{title}/{reward}"
        fun createRoute(place: String, title: String, reward: Int): String {
            return "work_complete/$place/$title/$reward"
        }
    }


    data object HistoryWorkScreen: Screen(title = "작업 기록", route = "history/work")
    data object HistoryRequestScreen: Screen(title = "의뢰 기록", route = "history/request")
    data object HistoryPointScreen: Screen(title = "포인트 내역", route = "history/point")

    data object AlarmSettingScreen: Screen(title = "알람 설정", route = "alarm")

    data object DetailStatsScreen : Screen(title = "세부 통계", route = "detail_stats/{region}/{category}/{statsId}") {
        fun createRoute(region: Region, category: Category, statsId: Int): String {
            return "detail_stats/${region.koreanName}/${category.koreanName}/$statsId"
        }
    }

    // 검색 관련 화면
    data object FilterSelectionScreen: Screen(title = "필터 선택", route = "filter_selection")
    data object SearchResultScreen: Screen(title = "검색 결과", route = "search_result")
    
    // 현재 의뢰 관련 화면
    data object CurrentRequestsFullScreen: Screen(title = "전체 현재 의뢰", route = "current_requests_full")

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
            bRoute = "request",
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
                LogoutConfirmScreen.route -> LogoutConfirmScreen

                NotificationScreen.route -> NotificationScreen

                RequestFormScreen.route -> RequestFormScreen
                RequestCompleteScreen.route -> RequestCompleteScreen

                AcceptRequestScreen.routeWithArg -> AcceptRequestScreen
                AcceptCompleteScreen.route -> AcceptCompleteScreen

                WorkListScreen.route -> WorkListScreen
                SubmitWorkScreen.route -> SubmitWorkScreen
                WorkCompleteScreen.route -> WorkCompleteScreen

                HistoryWorkScreen.route -> HistoryWorkScreen
                HistoryRequestScreen.route->HistoryRequestScreen
                HistoryPointScreen.route->HistoryPointScreen
                AlarmSettingScreen.route->AlarmSettingScreen

                DetailStatsScreen.route->DetailStatsScreen
                
                FilterSelectionScreen.route -> FilterSelectionScreen
                SearchResultScreen.route -> SearchResultScreen
                CurrentRequestsFullScreen.route -> CurrentRequestsFullScreen

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