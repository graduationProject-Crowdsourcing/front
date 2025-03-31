package project.graduation.crowd_sourcing.presentation.ui.screen.base

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import project.graduation.crowd_sourcing.presentation.R
import project.graduation.crowd_sourcing.presentation.ui.component.BottomBar
import project.graduation.crowd_sourcing.presentation.ui.component.TopBar
import project.graduation.crowd_sourcing.presentation.ui.navigation.Navigation
import project.graduation.crowd_sourcing.presentation.ui.navigation.Screen

@Composable
fun BaseView() {
    val navController = rememberNavController()
    val viewModel: BaseViewModel = viewModel()
    val uiState = viewModel.uiState.collectAsState()

    val context = LocalContext.current
    val apiKey = context.getString(R.string.google_maps_api_key)
    Log.d("API_KEY", "The API key is: $apiKey")

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