package project.graduation.crowd_sourcing.presentation.ui.component

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import project.graduation.crowd_sourcing.presentation.R
import project.graduation.crowd_sourcing.presentation.ui.screen.base.BaseUiState
import project.graduation.crowd_sourcing.presentation.utils.navigateBottom

@Composable
fun BottomBar(navController: NavController, uiState: BaseUiState) {
    NavigationBar(
        modifier = Modifier.wrapContentSize(),
        containerColor = Color.White,
        contentColor = Color.Black
    ) {
        val currentRoute =
            navController.currentBackStackEntryAsState().value?.destination?.route


        // example NavigationBarItem
        val screen = uiState.currentScreen
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(R.drawable.ic_launcher_foreground),
                    contentDescription = screen.title,
                    modifier = Modifier.height(16.dp)
                )
            },
            label = { Text(screen.title) },
            selected = currentRoute == screen.route,
            onClick = {
                navController.navigateBottom(screen)
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.Black,
                unselectedIconColor = Color.Gray,
                selectedTextColor = Color.Black,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color.Transparent
            )
        )
    }
}