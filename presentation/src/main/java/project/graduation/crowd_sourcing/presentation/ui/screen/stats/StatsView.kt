package project.graduation.crowd_sourcing.presentation.ui.screen.stats

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import project.graduation.crowd_sourcing.presentation.R
import project.graduation.crowd_sourcing.presentation.ui.screen.stats.component.StatsHorizontalChart

@Composable
fun StatsView() {
    val viewModel: StatsViewModel = hiltViewModel()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.space_medium))
    ) {
        StatsHorizontalChart()
    }
}

@Preview
@Composable
fun StatsViewPrev() {
    StatsView()
}