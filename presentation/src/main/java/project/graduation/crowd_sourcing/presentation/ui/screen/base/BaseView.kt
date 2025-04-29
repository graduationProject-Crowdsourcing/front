package project.graduation.crowd_sourcing.presentation.ui.screen.base

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.location.LocationServices
import project.graduation.crowd_sourcing.presentation.ui.component.bar.BottomBar
import project.graduation.crowd_sourcing.presentation.ui.component.bar.TopBar
import project.graduation.crowd_sourcing.presentation.ui.navigation.Navigation
import project.graduation.crowd_sourcing.presentation.ui.navigation.Screen


@Composable
fun BaseView() {
    val navController = rememberNavController()
    val viewModel: BaseViewModel = hiltViewModel()
    val uiState = viewModel.uiState.collectAsState()

    val context = LocalContext.current

    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    PermissionHandler()

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

@Composable
fun PermissionHandler() {
    val context = LocalContext.current

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        permissions.forEach { (perm, granted) ->
            Log.d("PermissionResult", "$perm granted: $granted")
        }

        // 위치 권한 허용 후 background location 권한 확인
        val locationGranted = permissions.entries.any {
            (it.key == Manifest.permission.ACCESS_FINE_LOCATION ||
                    it.key == Manifest.permission.ACCESS_COARSE_LOCATION) && it.value
        }

        if (locationGranted &&
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q &&
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", context.packageName, null)
            }
            context.startActivity(intent)
        }
    }

    LaunchedEffect(Unit) {
        val permissionsToRequest = mutableListOf<String>()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS)
        }

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            permissionsToRequest.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            permissionsToRequest.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }

        if (permissionsToRequest.isNotEmpty()) {
            locationPermissionLauncher.launch(permissionsToRequest.toTypedArray())
        } else if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", context.packageName, null)
            }
            context.startActivity(intent)
        }
    }
}
