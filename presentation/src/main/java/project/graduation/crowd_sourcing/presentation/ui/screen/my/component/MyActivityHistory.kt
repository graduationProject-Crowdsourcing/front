package project.graduation.crowd_sourcing.presentation.ui.screen.my.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import project.graduation.crowd_sourcing.presentation.R
import project.graduation.crowd_sourcing.presentation.ui.navigation.Screen
import project.graduation.crowd_sourcing.presentation.utils.spaceMedium
import project.graduation.crowd_sourcing.presentation.utils.spaceSmall
import project.graduation.crowd_sourcing.presentation.utils.textStyleLarge
import project.graduation.crowd_sourcing.presentation.utils.textStyleSmall

@Composable
fun MyActivityHistory(navController: NavController) {

    val historyList = listOf(
        (R.drawable.ic_history_work to "작업 기록") to {
            navController.navigate(Screen.HistoryWorkScreen.route)
        },
        (R.drawable.ic_history_request to "의뢰 기록") to {
            navController.navigate(Screen.HistoryRequestScreen.route)},
        (R.drawable.ic_history_point to "포인트 내역") to {}
    )

    Column(
        modifier = Modifier.padding(vertical = spaceMedium())
    ) {
        Text(
            text = "내 활동 내역",
            style = textStyleLarge(),
        )

        Spacer(modifier = Modifier.height(spaceSmall()))

        Row(
            modifier = Modifier.padding(horizontal = spaceMedium()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            historyList.forEach { history ->
                Box(
                    modifier = Modifier
                        .border(1.dp, color = colorResource(R.color.gray))
                        .weight(1f)
                        .clickable { history.second.invoke() },
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = spaceSmall())
                    ) {
                        Icon(
                            painter = painterResource(history.first.first),
                            contentDescription = null
                        )

                        Spacer(modifier = Modifier.height(spaceSmall()))

                        Text(
                            text = history.first.second,
                            style = textStyleSmall()
                        )
                    }
                }
            }
        }

    }
}

@Preview
@Composable
fun MyActivityHistoryPrev() {
    MyActivityHistory(rememberNavController())
}