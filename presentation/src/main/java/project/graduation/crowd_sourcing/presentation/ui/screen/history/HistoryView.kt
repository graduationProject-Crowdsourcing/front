package project.graduation.crowd_sourcing.presentation.ui.screen.history

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import project.graduation.crowd_sourcing.domain.model.Category
import project.graduation.crowd_sourcing.domain.model.Region
import project.graduation.crowd_sourcing.presentation.R
import project.graduation.crowd_sourcing.presentation.ui.navigation.Screen
import project.graduation.crowd_sourcing.presentation.ui.screen.history.component.HistoryList
import project.graduation.crowd_sourcing.presentation.ui.screen.history.component.HistoryStats

@Composable
fun HistoryView(
    historyType: HistoryType,
    navController: NavController
) {
    val viewModel: HistoryViewModel = hiltViewModel()
    val uiState = viewModel.uiState.collectAsState()

    val listTitles: Pair<List<String>, (String, String, Int) -> Unit>
    = if (historyType == HistoryType.WORK) {
        listOf("현재 작업 목록", "작업 기록") to {p1, p2, p3 -> }
    } else {
        listOf("현재 의뢰 목록", "의뢰 목록") to { region, category, statsId ->
            navController.navigate(Screen.DetailStatsScreen.createRoute(region = Region.from(region), category = Category.from(category), statsId = statsId))
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadHistoryData(historyType)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.space_medium))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            HistoryStats(type = uiState.value.stats.first)
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.space_medium)))

            HistoryStats(type = uiState.value.stats.second)
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.space_medium)))

            HistoryList(
                listTitle = listTitles.first[0],
                historyList = uiState.value.currentHistoryList,
                onClick = listTitles.second
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.space_medium)))

            HistoryList(
                listTitle = listTitles.first[1],
                historyList = uiState.value.totalHistoryList,
                onClick = listTitles.second
            )
        }
    }
}


@Preview
@Composable
fun HistoryViewPrev() {
    HistoryView(HistoryType.WORK, rememberNavController())
}