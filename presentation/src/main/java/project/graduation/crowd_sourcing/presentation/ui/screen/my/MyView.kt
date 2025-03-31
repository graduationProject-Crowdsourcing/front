package project.graduation.crowd_sourcing.presentation.ui.screen.my

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import project.graduation.crowd_sourcing.presentation.ui.screen.my.component.GrayDivider
import project.graduation.crowd_sourcing.presentation.ui.screen.my.component.MyActivityHistory
import project.graduation.crowd_sourcing.presentation.ui.screen.my.component.MyEtc
import project.graduation.crowd_sourcing.presentation.ui.screen.my.component.MyProfile
import project.graduation.crowd_sourcing.presentation.ui.screen.my.component.MyRecentActivity
import project.graduation.crowd_sourcing.presentation.utils.spaceMedium

@Composable
fun MyView(navController: NavController) {

    val viewModel: MyViewModel = hiltViewModel()
    val uiState = viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(spaceMedium())
            .verticalScroll(rememberScrollState())
    ) {
        MyProfile(uiState.value)
        GrayDivider()

        MyActivityHistory(navController)
        GrayDivider()

        MyRecentActivity(uiState.value)
        GrayDivider()

        MyEtc()
    }
}


@Preview
@Composable
fun MyViewPrev() {
    MyView(rememberNavController())
}
