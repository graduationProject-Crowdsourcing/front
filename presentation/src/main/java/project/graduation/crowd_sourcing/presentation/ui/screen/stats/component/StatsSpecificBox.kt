package project.graduation.crowd_sourcing.presentation.ui.screen.stats.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import project.graduation.crowd_sourcing.presentation.R
import project.graduation.crowd_sourcing.presentation.ui.screen.stats.StatsType
import project.graduation.crowd_sourcing.presentation.ui.screen.stats.StatsUiState

@Composable
fun StatsSpecificBox(uiState: StatsUiState) {
    val type = uiState.type
    val dataList = uiState.dataList
    val range = when(type){
        StatsType.MART -> uiState.requestRegion
        StatsType.PRODUCT -> uiState.requestProduct
    }


    val specificList = listOf(
        "$range 중 가장 저렴" to dataList.minByOrNull { it.price },
        "$range 중 가장 비싼" to dataList.maxByOrNull { it.price }
    )

    val average = dataList.map { it.price }.average()

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.space_medium))
    ) {
        specificList.chunked(2).forEach { pair ->
            pair.forEach { item ->
                Box(
                    modifier = Modifier
                        .weight(1f)
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
                            color = colorResource(R.color.darker_gary)
                        )
                        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.space_small)))

                        Text(
                            text = "${item.second?.name}\n" +
                                    "${item.second?.price.toString()}원",
                            style = TextStyle(fontSize = dimensionResource(id = R.dimen.sp_title).value.sp),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Left
                        )
                        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.space_small)))


                        Text(
                            text = item.second?.price?.let { getAverageDiff(average = average.toFloat(), value = it.toFloat()) } ?: "",
                            style = TextStyle(fontSize = dimensionResource(id = R.dimen.sp_small).value.sp),
                            color = if (item.second?.price?.toFloat() ?: 0f > average.toFloat()) colorResource(R.color.red) else colorResource(R.color.green)
                        )
                    }
                }
            }
        }
    }
}

private fun getAverageDiff(average: Float, value: Float): String {
    val diff = kotlin.math.abs(average - value)
    return if (value > average) {
        "평균 ${diff}원 이상 비쌈"
    } else {
        "평균 ${diff}원 이상 저렴"
    }
}

@Preview
@Composable
fun StatsSpecificBoxPrev(){
    val uiState = StatsUiState.test()
    StatsSpecificBox(uiState = uiState)
}