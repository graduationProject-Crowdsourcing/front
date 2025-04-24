package project.graduation.crowd_sourcing.presentation.ui.component.bar

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import project.graduation.crowd_sourcing.presentation.R
import project.graduation.crowd_sourcing.presentation.ui.component.GrayDivider
import project.graduation.crowd_sourcing.presentation.ui.navigation.Screen
import project.graduation.crowd_sourcing.presentation.ui.screen.base.BaseUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    uiState: BaseUiState,
    navController: NavController
) {
    Column {
        TopAppBar(
            title = {
                val isHomeScreen = uiState.currentScreen == Screen.BottomScreen.HomeScreen

                val screenTitle = if (isHomeScreen) {
                    stringResource(R.string.app)
                } else {
                    uiState.currentScreen.title
                }

                val textColor = if (isHomeScreen) {
                    colorResource(R.color.primary)
                } else {
                    Color.Black
                }

                Text(
                    text = screenTitle,
                    color = textColor,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = dimensionResource(id = R.dimen.sp_title).value.sp
                    )
                )
            },

            navigationIcon = {
                if (uiState.currentScreen !is Screen.BottomScreen) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_left),
                            contentDescription = "Back"
                        )
                    }
                }
            },

            actions = {
                if (uiState.currentScreen !is Screen.NotificationScreen){
                    IconButton(onClick = {
                        navController.navigate(Screen.NotificationScreen.route)
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_bell),
                            contentDescription = "notification"
                        )
                    }
                }

            }
        )

        GrayDivider()

    }
}

@Preview(showBackground = true)
@Composable
fun TopBarPrev() {
    TopBar(navController = rememberNavController(), uiState = BaseUiState.init())
}
