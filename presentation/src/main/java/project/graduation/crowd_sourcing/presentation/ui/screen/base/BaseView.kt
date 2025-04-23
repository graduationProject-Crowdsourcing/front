package project.graduation.crowd_sourcing.presentation.ui.screen.base

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.location.LocationServices
import project.graduation.crowd_sourcing.presentation.R
import project.graduation.crowd_sourcing.presentation.ui.component.Bar.BottomBar
import project.graduation.crowd_sourcing.presentation.ui.component.Bar.TopBar
import project.graduation.crowd_sourcing.presentation.ui.navigation.Navigation
import project.graduation.crowd_sourcing.presentation.ui.navigation.Screen


@Composable
fun BaseView() {
    val navController = rememberNavController()
    val viewModel: BaseViewModel = hiltViewModel()
    val uiState = viewModel.uiState.collectAsState()

    val context = LocalContext.current

    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)


    // 위치 추적 시작
    LaunchedEffect(Unit) {
        viewModel.startTracking(fusedLocationClient)
    }

    // Composable이 dispose될 때 위치 추적 중단
    DisposableEffect(Unit) {
        onDispose {
            viewModel.stopTracking(fusedLocationClient)
        }
    }

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