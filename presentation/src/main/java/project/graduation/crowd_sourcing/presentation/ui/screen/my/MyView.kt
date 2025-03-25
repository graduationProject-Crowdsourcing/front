package project.graduation.crowd_sourcing.presentation.ui.screen.my

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import project.graduation.crowd_sourcing.presentation.R
import project.graduation.crowd_sourcing.presentation.ui.component.GrayDivider
import project.graduation.crowd_sourcing.presentation.ui.screen.my.component.MyActivityHistory
import project.graduation.crowd_sourcing.presentation.ui.screen.my.component.MyEtc
import project.graduation.crowd_sourcing.presentation.ui.screen.my.component.MyProfile
import project.graduation.crowd_sourcing.presentation.ui.screen.my.component.MyRecentActivity

@Composable
fun MyView() {
    val viewModel: MyViewModel = hiltViewModel()
    val navController = rememberNavController()

    val uiState = viewModel.uiState.collectAsState()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.space_medium))
            .verticalScroll(rememberScrollState())
    ) {
        MyProfile(uiState.value)
        GrayDivider()

        MyActivityHistory()
        GrayDivider()

        MyRecentActivity(uiState.value)
        GrayDivider()

        MyEtc()
    }
}


@Preview
@Composable
fun MyViewPrev() {
    MyView()
}
