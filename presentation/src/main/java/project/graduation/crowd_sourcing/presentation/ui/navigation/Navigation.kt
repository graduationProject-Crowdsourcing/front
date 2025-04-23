package project.graduation.crowd_sourcing.presentation.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import project.graduation.crowd_sourcing.presentation.ui.screen.alarm.AlarmView
import project.graduation.crowd_sourcing.presentation.ui.screen.history.HistoryType
import project.graduation.crowd_sourcing.presentation.ui.screen.history.HistoryView
import project.graduation.crowd_sourcing.presentation.ui.screen.home.HomeView
import project.graduation.crowd_sourcing.presentation.ui.screen.login.LoginView
import project.graduation.crowd_sourcing.presentation.ui.screen.my.MyView
import project.graduation.crowd_sourcing.presentation.ui.screen.notification.NotificationView
import project.graduation.crowd_sourcing.presentation.ui.screen.point.PointView
import project.graduation.crowd_sourcing.presentation.ui.screen.request.TabRequestView
import project.graduation.crowd_sourcing.presentation.ui.screen.request.accept.AcceptCompleteView
import project.graduation.crowd_sourcing.presentation.ui.screen.request.accept.AcceptRequestView
import project.graduation.crowd_sourcing.presentation.ui.screen.request.request.RequestCompleteView
import project.graduation.crowd_sourcing.presentation.ui.screen.request.request.RequestFormView
import project.graduation.crowd_sourcing.presentation.ui.screen.request.work.SubmitWorkView
import project.graduation.crowd_sourcing.presentation.ui.screen.request.work.WorkCompleteView
import project.graduation.crowd_sourcing.presentation.ui.screen.request.work.WorkListView
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

        // 의뢰 탭 최초 화면
        composable(route = Screen.BottomScreen.RequestScreen.bRoute) {
            TabRequestView(navController = navController)
        }
        // 의뢰 작성 화면
        composable(route = Screen.RequestFormScreen.route) {
            RequestFormView(navController)
        }
        // 의뢰 완료 화면
        composable(route = Screen.RequestCompleteScreen.route) {
            RequestCompleteView(navController)
        }
        // 의뢰 수락 화면
        composable(route = Screen.AcceptRequestScreen.route) {
            AcceptRequestView(navController = navController)
        }

        // 의뢰 수락 완료 화면
        composable(route = Screen.AcceptCompleteScreen.route) { backStackEntry ->
            val place = backStackEntry.arguments?.getString("place") ?: ""
            val title = backStackEntry.arguments?.getString("title") ?: ""

            AcceptCompleteView(
                navController = navController,
                place = place,
                title = title
            )
        }

        // 작업 리스트 화면
        composable(route = Screen.WorkListScreen.route) {
            WorkListView(navController)
        }
        // 작업 제출 화면
        composable(route = Screen.SubmitWorkScreen.routeWithArg) { backStackEntry ->
            val workId = backStackEntry.arguments?.getString("workId")
            SubmitWorkView(navController = navController, workId = workId)
        }
        // 작업 완료 화면
        composable(
            route = Screen.WorkCompleteScreen.routeWithArgs,
            arguments = listOf(
                navArgument("place") { type = NavType.StringType },
                navArgument("title") { type = NavType.StringType },
                navArgument("reward") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val place = backStackEntry.arguments?.getString("place") ?: ""
            val title = backStackEntry.arguments?.getString("title") ?: ""
            val reward = backStackEntry.arguments?.getInt("reward") ?: 0

            WorkCompleteView(navController, place, title, reward)
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