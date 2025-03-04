package project.graduation.crowd_sourcing.presentation.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import project.graduation.crowd_sourcing.presentation.ui.screen.login.LoginView

@Composable
fun Navigation(
    navController: NavHostController,
    pd: PaddingValues
){
    NavHost(
        navController = navController,
        startDestination = Screen.LoginScreen.route,
        modifier = Modifier.padding(pd)
    ){
        composable(route = Screen.LoginScreen.route){
            LoginView()
        }
    }
}