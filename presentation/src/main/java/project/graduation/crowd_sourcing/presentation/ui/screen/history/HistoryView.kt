package project.graduation.crowd_sourcing.presentation.ui.screen.history

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import project.graduation.crowd_sourcing.presentation.ui.screen.history.component.HistoryStats
import project.graduation.crowd_sourcing.presentation.utils.spaceMedium

@Composable
fun HistoryView(historyType: HistoryType){
    val viewModel:HistoryViewModel = hiltViewModel()
    val uiState = viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(spaceMedium())
    ) {
        HistoryStats(type = uiState.value.stats.first)

        Spacer(modifier = Modifier.height(spaceMedium()))
        HistoryStats(type = uiState.value.stats.second)
    }
}