package project.graduation.crowd_sourcing.presentation.ui.screen.base

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import project.graduation.crowd_sourcing.presentation.ui.component.BottomBar
import project.graduation.crowd_sourcing.presentation.ui.component.TopBar
import project.graduation.crowd_sourcing.presentation.ui.navigation.Navigation

@Composable
fun BaseView() {
    val navController = rememberNavController()

    val viewModel: BaseViewModel = viewModel()
    val uiState = viewModel.uiState.collectAsState()

    LaunchedEffect(navController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val route = destination.route
            route?.let {
                viewModel.updateCurrentScreen(navController)
            }
        }
    }


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBar(
                uiState = uiState.value,
                navController = navController
            )
        },
        bottomBar = {
            if (uiState.value.currentScreen is Screen.BottomScreen) {
                BottomBar(
                    navController = navController,
                    uiState = uiState.value
                )
            }
        }
    ) { paddingValues ->
        Navigation(
            navController = navController,
            pd = paddingValues
        )
    }
}