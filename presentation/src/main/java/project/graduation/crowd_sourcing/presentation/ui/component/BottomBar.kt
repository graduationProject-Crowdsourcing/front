package project.graduation.crowd_sourcing.presentation.ui.component

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import project.graduation.crowd_sourcing.presentation.R
import project.graduation.crowd_sourcing.presentation.ui.navigation.screensInBottom
import project.graduation.crowd_sourcing.presentation.ui.screen.base.BaseUiState
import project.graduation.crowd_sourcing.presentation.utils.navigateBottom

@Composable
fun BottomBar(navController: NavController, uiState: BaseUiState) {
    NavigationBar(
        modifier = Modifier.wrapContentSize(),
        containerColor = Color.White,
        contentColor = Color.Black
    ) {
        screensInBottom.forEach { screen ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(screen.icon),
                        contentDescription = screen.title,
                        modifier = Modifier.size(30.dp)
                    )
                },
                label = { Text(text = screen.title, style = TextStyle(fontSize = 10.sp)) },
                selected = uiState.currentScreen.route == screen.route,
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
}