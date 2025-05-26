package project.graduation.crowd_sourcing.presentation.ui.screen.my

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import project.graduation.crowd_sourcing.presentation.R
import project.graduation.crowd_sourcing.presentation.ui.component.GrayDivider
import project.graduation.crowd_sourcing.presentation.ui.screen.my.component.MyActivityHistory
import project.graduation.crowd_sourcing.presentation.ui.screen.my.component.MyEtc
import project.graduation.crowd_sourcing.presentation.ui.screen.my.component.MyProfile
import project.graduation.crowd_sourcing.presentation.ui.screen.my.component.MyProfileEditDialog
import project.graduation.crowd_sourcing.presentation.ui.screen.my.component.MyRecentActivity

@Composable
fun MyView(navController: NavController) {
    val viewModel: MyViewModel = hiltViewModel()
    val uiState = viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getRecentHistory()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.space_medium))
            .verticalScroll(rememberScrollState())
    ) {
        MyProfile(
            myUiState = uiState.value,
            profileEdit = {
                viewModel.setDialogVisibility(true)
            }
        )
        GrayDivider()

        MyActivityHistory(navController)
        GrayDivider()

        MyRecentActivity(uiState.value, navController = navController)
        GrayDivider()

        MyEtc(navController)

    }

    MyProfileEditDialog(
        onDismiss = { viewModel.setDialogVisibility(false) },
        onSave = { nickname, profileImage ->
            viewModel.setDialogVisibility(false)
        },
        uiState = uiState.value
    )
}


@Preview
@Composable
fun MyViewPrev() {
    MyView(rememberNavController())
}
