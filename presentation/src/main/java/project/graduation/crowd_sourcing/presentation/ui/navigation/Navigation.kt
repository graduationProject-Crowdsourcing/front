package project.graduation.crowd_sourcing.presentation.ui.navigation

import android.graphics.Point
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import project.graduation.crowd_sourcing.presentation.ui.screen.alarm.AlarmView
import project.graduation.crowd_sourcing.presentation.ui.screen.history.HistoryType
import project.graduation.crowd_sourcing.presentation.ui.screen.history.HistoryView
import project.graduation.crowd_sourcing.presentation.ui.screen.home.HomeView
import project.graduation.crowd_sourcing.presentation.ui.screen.login.LoginView
import project.graduation.crowd_sourcing.presentation.ui.screen.my.MyView
import project.graduation.crowd_sourcing.presentation.ui.screen.notification.NotificationView
import project.graduation.crowd_sourcing.presentation.ui.screen.point.PointView
import project.graduation.crowd_sourcing.presentation.ui.screen.request.request.RequestFormView
import project.graduation.crowd_sourcing.presentation.ui.screen.request.request.RequestView
import project.graduation.crowd_sourcing.presentation.ui.screen.search.FilterSelectionView
import project.graduation.crowd_sourcing.presentation.ui.screen.search.SearchResultView
import project.graduation.crowd_sourcing.presentation.ui.screen.search.SearchView
import project.graduation.crowd_sourcing.presentation.ui.screen.stats.StatsView

@Composable
fun Navigation(
    navController: NavHostController,
    pd: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = Screen.BottomScreen.HomeScreen.bRoute,
        modifier = Modifier.padding(pd)
    ) {
        composable(route = Screen.LoginScreen.route) {
            LoginView(navController = navController)
        }

        composable(route = Screen.BottomScreen.HomeScreen.bRoute) {
            HomeView()
        }

        composable(route = Screen.BottomScreen.SearchScreen.bRoute) {
            SearchView(navController = navController)
        }

        // 통합 필터 선택 화면 (카테고리 및 지역 선택)
        composable(route = Screen.FilterSelectionScreen.route) {
            FilterSelectionView(navController = navController)
        }

        // 검색 결과 화면
        composable(route = Screen.SearchResultScreen.route) {
            SearchResultView(navController = navController)
        }

        composable(route = Screen.BottomScreen.RequestScreen.bRoute) {
            RequestView(navController = navController)
        }
        composable(route = Screen.RequestFormScreen.route) {
            RequestFormView(navController)
        }
        composable(route = Screen.RequestFormScreen.route) {
            RequestFormView(navController)
        }

        composable(route = Screen.BottomScreen.MyScreen.bRoute) {
            MyView(navController)
        }

        composable(route = Screen.HistoryWorkScreen.route) {
            HistoryView(HistoryType.WORK, navController)
        }
        composable(route = Screen.HistoryRequestScreen.route) {
            HistoryView(HistoryType.REQUEST, navController)
        }
        composable(route = Screen.HistoryPointScreen.route) {
            PointView()
        }

        composable(route = Screen.AlarmSettingScreen.route) {
            AlarmView()
        }

        composable(route = Screen.NotificationScreen.route) {
            NotificationView()
        }

        composable(route = Screen.DetailStatsScreen.route) {
            StatsView()
        }
    }
}