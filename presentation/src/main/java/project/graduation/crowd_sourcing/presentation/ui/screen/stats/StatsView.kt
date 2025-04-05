package project.graduation.crowd_sourcing.presentation.ui.screen.stats

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import project.graduation.crowd_sourcing.presentation.R
import project.graduation.crowd_sourcing.presentation.ui.screen.stats.component.StatsHorizontalChart
import project.graduation.crowd_sourcing.presentation.ui.screen.stats.component.StatsRequestedTerm
import project.graduation.crowd_sourcing.presentation.ui.screen.stats.component.StatsResultBox
import project.graduation.crowd_sourcing.presentation.ui.screen.stats.component.StatsResultList

@Composable
fun StatsView() {
    val viewModel: StatsViewModel = hiltViewModel()
    val uiState = viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.space_medium))
    ) {
        StatsRequestedTerm(uiState = uiState.value)
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.space_medium)))

        StatsResultBox(
            dataList = uiState.value.dataList,
            type = uiState.value.type
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.space_medium)))


    }
}

@Preview
@Composable
fun StatsViewPrev() {
    StatsView()
}