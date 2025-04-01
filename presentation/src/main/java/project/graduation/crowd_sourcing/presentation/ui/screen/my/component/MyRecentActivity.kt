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
import project.graduation.crowd_sourcing.presentation.R
import project.graduation.crowd_sourcing.presentation.ui.component.CommonListItem
import project.graduation.crowd_sourcing.presentation.ui.screen.my.MyUiState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

@Composable
fun MyRecentActivity(myUiState: MyUiState) {
    Column(
        modifier = Modifier.padding(vertical = dimensionResource(R.dimen.space_small))
    ) {
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.space_small)))

        Text(
            text = "최근 작업",
            style = TextStyle(fontSize = dimensionResource(id = R.dimen.sp_large).value.sp)
        )


        myUiState.recentWork.forEach { work->
            work.run {
                CommonListItem(
                    mainText = name,
                    subText = getTimeAgo(date),
                    icon = R.drawable.ic_list_box,
                    onClick = {}
                )
            }
        }

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.space_small)))

        Text(
            text = "최근 의뢰",
            style = TextStyle(fontSize = dimensionResource(id = R.dimen.sp_large).value.sp)
        )

        myUiState.recentRequest.forEach { request->
            request.run {
                CommonListItem(
                    mainText = name,
                    subText = getTimeAgo(date),
                    icon = R.drawable.ic_list_box,
                    onClick = {}
                )
            }
        }
    }
}



fun getTimeAgo(date: Date): String {
    val now = System.currentTimeMillis()
    val time = date.time
    val diff = now - time

    return when {
        diff < TimeUnit.MINUTES.toMillis(1) -> "방금 전"
        diff < TimeUnit.HOURS.toMillis(1) -> "${TimeUnit.MILLISECONDS.toMinutes(diff)}분 전"
        diff < TimeUnit.DAYS.toMillis(1) -> "${TimeUnit.MILLISECONDS.toHours(diff)}시간 전"
        diff < TimeUnit.DAYS.toMillis(7) -> "${TimeUnit.MILLISECONDS.toDays(diff)}일 전"
        else -> SimpleDateFormat("yyyy.MM.dd", Locale.getDefault()).format(date)
    }
}
@Preview
@Composable
fun MyRecentActivityPrev(){
    MyRecentActivity(myUiState = MyUiState.init())
}