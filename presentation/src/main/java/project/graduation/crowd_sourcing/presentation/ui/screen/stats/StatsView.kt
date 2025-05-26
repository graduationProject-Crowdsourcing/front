package project.graduation.crowd_sourcing.presentation.ui.screen.stats

import android.os.Build
import androidx.annotation.RequiresApi
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
import project.graduation.crowd_sourcing.domain.model.Category
import project.graduation.crowd_sourcing.domain.model.Region
import project.graduation.crowd_sourcing.presentation.R
import project.graduation.crowd_sourcing.presentation.ui.screen.stats.component.StatsRequestedTerm
import project.graduation.crowd_sourcing.presentation.ui.screen.stats.component.StatsResultBox
import project.graduation.crowd_sourcing.presentation.ui.screen.stats.component.StatsSpecificBox

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StatsView(
    region: Region,
    category: Category,
    id: Int
) {
    val viewModel: StatsViewModel = hiltViewModel()
    val uiState = viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getDetail(id)
    }

    LaunchedEffect(uiState.value.type) {
        viewModel.getDataList(region = region, category = category)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(dimensionResource(R.dimen.space_medium))
    ) {
        StatsRequestedTerm(uiState = uiState.value)
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.space_medium)))

        if (uiState.value.dataList.isNotEmpty()) {
            StatsResultBox(
                uiState = uiState.value,
                switchOtherType = { viewModel.onSwitchType() }
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.space_medium)))

            StatsSpecificBox(uiState = uiState.value)
        }
    }
}


@Preview
@Composable
fun StatsViewPrev() {
}