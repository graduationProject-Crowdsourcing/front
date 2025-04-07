package project.graduation.crowd_sourcing.presentation.ui.screen.stats.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import project.graduation.crowd_sourcing.presentation.R
import project.graduation.crowd_sourcing.presentation.ui.screen.stats.StatsUiState
import java.text.SimpleDateFormat
import java.util.Date


@Composable
fun StatsRequestedTerm(uiState: StatsUiState) {
    val list = listOf(
        "해당 지역" to uiState.requestRegion,
        "의뢰 기간" to formatRequestPeriod(uiState.requestStartDate,uiState.requestCompleteDate),
        "제품" to uiState.requestProduct
    )
    Column {
        Text(
            text = "의뢰 사항",
            style = TextStyle(fontSize = dimensionResource(id = R.dimen.sp_large).value.sp)
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.space_small)))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.space_small))
        ) {
            items(list) { item ->
                Box(
                    modifier = Modifier
                        .width(120.dp)
                        .border(
                            1.dp,
                            color = colorResource(R.color.gray),
                            shape = RoundedCornerShape(6.dp)
                        )
                ) {
                    Column(
                        Modifier.padding(dimensionResource(R.dimen.space_small))
                    ) {
                        Text(
                            text = item.first,
                            style = TextStyle(fontSize = dimensionResource(id = R.dimen.sp_small).value.sp),
                            color = colorResource(R.color.black50)
                        )
                        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.space_small)))
                        Text(
                            text = item.second.toString(),
                            style = TextStyle(fontSize = dimensionResource(id = R.dimen.sp_medium).value.sp),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

private fun formatRequestPeriod(startDate: Date, endDate: Date): String {
    val formatter = SimpleDateFormat("M.d") // Format as "Month.Day"
    return formatter.format(startDate) + "~" + formatter.format(endDate)
}