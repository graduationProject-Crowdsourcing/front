package project.graduation.crowd_sourcing.presentation.utils

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import project.graduation.crowd_sourcing.presentation.ui.navigation.Screen

fun NavController.navigateBottom(screen: Screen) {
    navigate(screen.route) {
        popUpTo(graph.findStartDestination().route ?: return@navigate) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}