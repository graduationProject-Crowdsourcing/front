package project.graduation.crowd_sourcing.presentation.ui.screen.point

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import project.graduation.crowd_sourcing.presentation.R
import project.graduation.crowd_sourcing.presentation.ui.component.CommonListItem
import project.graduation.crowd_sourcing.presentation.utils.getTimeAgo

@Composable
fun PointView() {
    val viewModel: PointViewModel = hiltViewModel()
    val uiState = viewModel.uiState.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.space_medium))
    ) {
        LazyColumn {
            items(uiState.value.list) { item ->
                item.run {
                    CommonListItem(
                        mainText = type.type,
                        subText = "${getTimeAgo(date)} / ${region} / ${name}",
                        leftText = "${point} points",
                        icon = R.drawable.ic_list_box,
                        onClick = { }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PointViewPrev() {
    PointView()
}