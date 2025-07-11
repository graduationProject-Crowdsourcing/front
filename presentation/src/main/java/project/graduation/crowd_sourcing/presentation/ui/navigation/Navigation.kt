package project.graduation.crowd_sourcing.presentation.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import project.graduation.crowd_sourcing.domain.model.Category
import project.graduation.crowd_sourcing.domain.model.Region
import project.graduation.crowd_sourcing.presentation.ui.screen.alarm.AlarmView
import project.graduation.crowd_sourcing.presentation.ui.screen.history.HistoryType
import project.graduation.crowd_sourcing.presentation.ui.screen.history.HistoryView
import project.graduation.crowd_sourcing.presentation.ui.screen.home.HomeView
import project.graduation.crowd_sourcing.presentation.ui.screen.login.LoginView
import project.graduation.crowd_sourcing.presentation.ui.screen.logout.LogoutConfirmView
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
import project.graduation.crowd_sourcing.presentation.ui.screen.home.component.CurrentRequestsFullView
import project.graduation.crowd_sourcing.presentation.ui.screen.request.request.SelectRegionView

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation(
    navController: NavHostController,
    pd: PaddingValues,
    isInitialized: Boolean
) {
    NavHost(
        navController = navController,
        // startDestination = Screen.BottomScreen.HomeScreen.bRoute,
        startDestination = if (isInitialized) Screen.BottomScreen.HomeScreen.bRoute else Screen.LoginScreen.route,

        modifier = Modifier
            .padding(pd)
            .fillMaxSize(),
        enterTransition = {
            fadeIn(animationSpec = tween(durationMillis = 300))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(durationMillis = 300))
        },
        popEnterTransition = { fadeIn() },
        popExitTransition = { fadeOut() }
    ) {
        composable(route = Screen.LoginScreen.route) {
            LoginView(navController = navController)
        }

        composable(route = Screen.LogoutConfirmScreen.route) {
            LogoutConfirmView(navController = navController)
        }


        composable(route = Screen.BottomScreen.HomeScreen.bRoute) {
            HomeView(navController = navController)
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
        
        // 전체 현재 의뢰 화면
        composable(route = Screen.CurrentRequestsFullScreen.route) {
            CurrentRequestsFullView(navController = navController)
        }

        // 의뢰 탭 최초 화면
        composable(route = Screen.BottomScreen.RequestScreen.bRoute) {
            TabRequestView(navController = navController)
        }
        // 의뢰 작성 화면
        composable(route = Screen.RequestFormScreen.route) {
            RequestFormView(navController)
        }

        // 의뢰 작성 - 지역 선택 화면
        composable(route = Screen.SelectRegionScreen.route) {
            SelectRegionView(
                navController = navController,
                onConfirmSelection = { selectedRegions ->
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("selectedRegions", selectedRegions)
                    navController.popBackStack()
                }
            )
        }

        // 의뢰 완료 화면
        composable(route = Screen.RequestCompleteScreen.route) {
            RequestCompleteView(navController)
        }
        // 의뢰 수락 화면
        composable(
            route = Screen.AcceptRequestScreen.routeWithArg,
            arguments = listOf(navArgument("commissionId") { type = NavType.IntType })
        ) { backStackEntry ->
            val commissionId = backStackEntry.arguments?.getInt("commissionId") ?: 0
            AcceptRequestView(
                navController = navController,
                commissionId = commissionId
            )
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
        composable(
            route = Screen.SubmitWorkScreen.routeWithArg,
            arguments = listOf(
                navArgument("workId") { type = NavType.IntType },
                navArgument("category") { type = NavType.StringType },
                navArgument("martName") { type = NavType.StringType })
        ) { backStackEntry ->
            val workId = backStackEntry.arguments?.getInt("workId") ?: 0
            val category =  backStackEntry.arguments?.getString("category") ?: ""
            val martName =  backStackEntry.arguments?.getString("martName") ?: ""
            SubmitWorkView(navController = navController, workId = workId, martName = martName, category = category)
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

        composable(
            route = Screen.DetailStatsScreen.route,
            arguments = listOf(
                navArgument("region") { type = NavType.StringType },
                navArgument("category") { type = NavType.StringType },
                navArgument("statsId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val region = backStackEntry.arguments?.getString("region") ?: ""
            val category = backStackEntry.arguments?.getString("category") ?: ""
            val id = backStackEntry.arguments?.getInt("statsId") ?: 0

            StatsView(region = region, category = category, id = id)
        }
    }
}