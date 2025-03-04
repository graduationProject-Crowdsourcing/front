package project.graduation.crowd_sourcing.presentation.ui.navigation

sealed class Screen(val title:String, val route: String) {
    data object LoginScreen: Screen(title = "Login", route = "login")

    companion object {
        fun fromRoute(route: String?): Screen {
            return when (route) {
                LoginScreen.route -> LoginScreen
                else -> LoginScreen
            }
        }
    }
}