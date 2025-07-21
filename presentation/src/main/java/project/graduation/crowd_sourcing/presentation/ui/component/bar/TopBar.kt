package project.graduation.crowd_sourcing.presentation.ui.component.bar

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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

                if (isHomeScreen) {
                    // 홈 화면에서는 로고 이미지 사용 (왼쪽 정렬)
                    Box(
                        modifier = androidx.compose.ui.Modifier.fillMaxWidth()
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.logo_price_it_no_text),
                            contentDescription = "PRICE-IT 로고",
                            modifier = androidx.compose.ui.Modifier
                                .height(32.dp)
                                .width(120.dp)
                                .offset(x = (-8).dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                } else {
                    // 다른 화면에서는 텍스트 사용
                    Text(
                        text = uiState.currentScreen.title,
                        color = Color.Black,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = dimensionResource(id = R.dimen.sp_title).value.sp
                        )
                    )
                }
            },

            navigationIcon = {
                if (uiState.currentScreen !is Screen.BottomScreen && uiState.currentScreen !is Screen.LoginScreen) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_left),
                            contentDescription = "Back"
                        )
                    }
                }
            },

            actions = {
                if (uiState.currentScreen is Screen.BottomScreen){
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
