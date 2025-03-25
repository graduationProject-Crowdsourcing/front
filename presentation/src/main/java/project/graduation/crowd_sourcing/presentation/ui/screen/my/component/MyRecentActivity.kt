package project.graduation.crowd_sourcing.presentation.ui.screen.my.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import project.graduation.crowd_sourcing.presentation.R
import project.graduation.crowd_sourcing.presentation.ui.screen.my.MyUiState
import project.graduation.crowd_sourcing.presentation.utils.spaceSmall
import project.graduation.crowd_sourcing.presentation.utils.textStyleLarge
import project.graduation.crowd_sourcing.presentation.utils.textStyleSmall
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

@Composable
fun MyRecentActivity(myUiState: MyUiState) {
    Column(
        modifier = Modifier.padding(vertical = spaceSmall())
    ) {
        Spacer(modifier = Modifier.height(spaceSmall()))

        Text(
            text = "최근 작업",
            style = textStyleLarge()
        )


        myUiState.recentWork.forEach { work->
            RecentHistoryItem(work)
        }

        Spacer(modifier = Modifier.height(spaceSmall()))

        Text(
            text = "최근 의뢰",
            style = textStyleLarge()
        )

        myUiState.recentRequest.forEach { request->
            RecentHistoryItem(request)
        }
    }
}

@Composable
fun RecentHistoryItem(
    item: MyUiState.RecentListItem
){
    Row(
        modifier = Modifier
            .clickable {  }
            .padding(vertical = spaceSmall())
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_list_box),
            contentDescription = null
        )

        Spacer(modifier = Modifier.width(spaceSmall()))

        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            Text(
                text = item.name,
                style = textStyleLarge(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = getTimeAgo(item.date),
                color = colorResource(R.color.darker_gary),
                style = textStyleSmall(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
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