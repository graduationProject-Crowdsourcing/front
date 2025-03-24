package project.graduation.crowd_sourcing.presentation.ui.screen.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.maps.model.LatLng
import project.graduation.crowd_sourcing.presentation.ui.screen.home.component.MapSection
import project.graduation.crowd_sourcing.presentation.ui.screen.home.component.RequestsSection
import project.graduation.crowd_sourcing.presentation.ui.screen.home.component.SearchSection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView() {
    val viewModel: HomeViewModel = hiltViewModel()
    val uiState = viewModel.uiState.collectAsState()
    val context = LocalContext.current
    
    // Google Maps 사용 가능 여부 확인
    val isGoogleMapsAvailable = remember {
        val availability = GoogleApiAvailability.getInstance()
        val resultCode = availability.isGooglePlayServicesAvailable(context)
        resultCode == com.google.android.gms.common.ConnectionResult.SUCCESS
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item { MapSection(isGoogleMapsAvailable, uiState) }
        item { SearchSection(uiState.value.searchQuery, viewModel::updateSearchQuery) }
        item {
            RequestsSection(viewModel = viewModel, uiState = uiState)
        }
    }
}

fun Location.toLatLng(): LatLng {
    return LatLng(this.latitude, this.longitude)
}

//@Preview(showBackground = true)
//@Composable
//fun CurrentRequestsListPreview(){
//    CrowdSourcingTheme{
//        CurrentRequestsList()
//    }
//
//}
