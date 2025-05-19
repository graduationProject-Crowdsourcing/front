package project.graduation.crowd_sourcing.presentation.ui.screen.my.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import project.graduation.crowd_sourcing.presentation.R
import project.graduation.crowd_sourcing.presentation.ui.component.GrayDivider
import project.graduation.crowd_sourcing.presentation.ui.component.list.CommonList
import project.graduation.crowd_sourcing.presentation.ui.component.list.CommonListItem
import project.graduation.crowd_sourcing.presentation.ui.component.list.CommonListItemData
import project.graduation.crowd_sourcing.presentation.ui.navigation.Screen
import project.graduation.crowd_sourcing.presentation.ui.screen.my.MyUiState
import project.graduation.crowd_sourcing.presentation.utils.getTimeAgo

@Composable
fun MyRecentActivity(myUiState: MyUiState, navController: NavController) {
    Column(
        modifier = Modifier.padding(vertical = dimensionResource(R.dimen.space_small))
    ) {
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.space_small)))

        Text(
            text = "최근 작업",
            style = TextStyle(fontSize = dimensionResource(id = R.dimen.sp_large).value.sp)
        )

        myUiState.recentWork.run {
            CommonListItem(
                CommonListItemData(
                    mainText = name,
                    subText = getTimeAgo(date),
                    icon = R.drawable.ic_list_box,
                    onClick = {}
                )
            )
        }

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.space_small)))

        Text(
            text = "최근 의뢰",
            style = TextStyle(fontSize = dimensionResource(id = R.dimen.sp_large).value.sp)
        )

        myUiState.recentRequest.run {
            CommonListItem(
                CommonListItemData(
                    mainText = name,
                    subText = getTimeAgo(date),
                    icon = R.drawable.ic_list_box,
                    onClick = { navController.navigate(Screen.DetailStatsScreen.route) }
                )
            )
        }
    }
}


@Preview
@Composable
fun MyRecentActivityPrev() {
    MyRecentActivity(myUiState = MyUiState.init(), navController = rememberNavController())
}