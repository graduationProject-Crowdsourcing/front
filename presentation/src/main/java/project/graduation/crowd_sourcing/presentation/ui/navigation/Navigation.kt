package project.graduation.crowd_sourcing.presentation.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import project.graduation.crowd_sourcing.presentation.ui.screen.history.HistoryType
import project.graduation.crowd_sourcing.presentation.ui.screen.history.HistoryView
import project.graduation.crowd_sourcing.presentation.ui.screen.login.LoginView
import project.graduation.crowd_sourcing.presentation.ui.screen.my.MyView
import project.graduation.crowd_sourcing.presentation.ui.screen.home.HomeView
import project.graduation.crowd_sourcing.presentation.ui.screen.notification.NotificationView
import project.graduation.crowd_sourcing.presentation.ui.screen.search.SearchView

@Composable
fun Navigation(
    navController: NavHostController,
    pd: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = Screen.LoginScreen.route,
        modifier = Modifier.padding(pd)
    ) {
        composable(route = Screen.LoginScreen.route) {
            LoginView(navController = navController)
        }

        composable(route = Screen.BottomScreen.HomeScreen.bRoute) {
            HomeView()
        }

        composable(route = Screen.BottomScreen.SearchScreen.bRoute) {
            SearchView()
        }

        composable(route = Screen.BottomScreen.RequestScreen.bRoute) {

        }

        composable(route = Screen.BottomScreen.MyScreen.bRoute) {
            MyView(navController)
        }

        composable(route = Screen.HistoryWorkScreen.route) {
            HistoryView(HistoryType.Work)
        }
        composable(route = Screen.HistoryRequestScreen.route) {
            HistoryView(HistoryType.Request)
        }

        composable(route = Screen.NotificationScreen.route) {
            NotificationView()
        }
    }
}