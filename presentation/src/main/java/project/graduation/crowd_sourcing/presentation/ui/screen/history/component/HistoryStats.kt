package project.graduation.crowd_sourcing.presentation.ui.screen.history.component

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import project.graduation.crowd_sourcing.presentation.R
import project.graduation.crowd_sourcing.presentation.ui.screen.history.HistoryUiState

@Composable
fun HistoryStats(type: HistoryUiState.StatsType) {
    val list = HistoryUiState.getStatsList(type)

    Column {
        Text(
            text = when (type) {
                is HistoryUiState.StatsType.Request.All,
                is HistoryUiState.StatsType.Work.All -> "전체 통계"

                is HistoryUiState.StatsType.Request.Detail,
                is HistoryUiState.StatsType.Work.Detail -> "세부 통계"

                HistoryUiState.StatsType.Init -> ""
            },
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
                        .border(1.dp, color = colorResource(R.color.gray), shape = RoundedCornerShape(6.dp))
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


@Preview
@Composable
fun HistoryStatsPrev() {
    HistoryStats(type = HistoryUiState.StatsType.Work.All(0, 0, 0))
}