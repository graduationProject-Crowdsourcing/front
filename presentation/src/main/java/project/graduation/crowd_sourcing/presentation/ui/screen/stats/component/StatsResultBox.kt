package project.graduation.crowd_sourcing.presentation.ui.screen.stats.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text
import androidx.xr.compose.testing.toDp
import project.graduation.crowd_sourcing.presentation.R
import project.graduation.crowd_sourcing.presentation.ui.component.GrayDivider
import project.graduation.crowd_sourcing.presentation.ui.screen.stats.StatsType
import project.graduation.crowd_sourcing.presentation.ui.screen.stats.StatsUiState

@Composable
fun StatsResultBox(
    uiState: StatsUiState,
    switchOtherType: () -> Unit = {}
) {
    val dataList = uiState.dataList
    val type = uiState.type

    Column {
        StatsSwitchType(
            switchOtherType = switchOtherType,
            type = type
        )
        Spacer(Modifier.height(dimensionResource(R.dimen.space_small)))

        Box(
            Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = colorResource(R.color.gray),
                    shape = RoundedCornerShape(dimensionResource(R.dimen.round_common))
                ),
            contentAlignment = Alignment.Center
        ) {
            Column {
                StatsResultList(
                    dataList = dataList,
                    type = type
                )

                GrayDivider()

                StatsHorizontalChart(chartList = dataList)
            }
        }
    }
}

@Composable
fun StatsHorizontalChart(chartList: List<StatsUiState.StatsListItem>) {
    // data set
    val labels = chartList.map { it.name }

    val maxPrice = roundToSecondHighestFiveUnit(chartList.maxOf { it.price })
    val barWidthsRatio = chartList.map { it.price.toFloat() / maxPrice }

    // dp
    val barThicknessDp = 28.dp
    val barSpacingDp = 20.dp

    // dp to px
    val density = LocalDensity.current
    val barThicknessPx = with(density) { barThicknessDp.toPx() }
    val barSpacingPx = with(density) { barSpacingDp.toPx() }
    val textPaintRight = android.graphics.Paint().apply {
        color = colorResource(R.color.darker_gary).toArgb()
        textSize = with(density) { dimensionResource(R.dimen.sp_small).toPx() }
        textAlign = android.graphics.Paint.Align.RIGHT
    }
    val textPaintCenter = android.graphics.Paint().apply {
        color = colorResource(R.color.darker_gary).toArgb()
        textSize = with(density) { dimensionResource(R.dimen.space_small).toPx() }
        textAlign = android.graphics.Paint.Align.CENTER
    }


    val barColor = colorResource(R.color.primary)
    val dividerColor = colorResource(R.color.gray)

    val chartHeightDp = (chartList.size * (barThicknessPx + barSpacingPx) - barSpacingPx).toDp()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = dimensionResource(R.dimen.sp_medium)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(dimensionResource(R.dimen.space_medium))
                .height(chartHeightDp)
        ) {
            val dividerX = 160f

            val numLines = 5
            val spacing = (size.width - dividerX) / (numLines - 1)
            for (i in 0 until numLines) {
                val currentX = dividerX + (i * spacing)
                drawLine(
                    color = dividerColor,
                    start = Offset(currentX, -10f),
                    end = Offset(currentX, size.height + 10f),
                    strokeWidth = if (i == 0) 4f else 2f
                )

                drawContext.canvas.nativeCanvas.drawText(
                    "${maxPrice / 4 * i}원",
                    currentX,
                    size.height + 40f,
                    textPaintCenter
                )
            }


            barWidthsRatio.forEachIndexed { index, ratio ->
                val yOffset = index * (barThicknessPx + barSpacingPx)

                val width = (size.width - dividerX - 10f) * ratio
                drawRect(
                    color = barColor,
                    topLeft = Offset(dividerX + 10f, yOffset),
                    size = Size(width, barThicknessPx)
                )

//                val splitLabel = labels[index].split(" ")
//                splitLabel.forEachIndexed { lineIndex, lineText ->
//                    val totalTextHeight = (splitLabel.size - 1) * 45f
//                    val textBlockCenterOffset = totalTextHeight / 2
//
//                    drawContext.canvas.nativeCanvas.drawText(
//                        lineText,
//                        dividerX - 20f,
//                        yOffset + barThicknessPx / 2 - textBlockCenterOffset + (lineIndex * 45f) + 15f, // 중앙에 배치되도록 위치 조정
//                        textPaint
//                    )
//                }
                val label = labels[index]
                drawContext.canvas.nativeCanvas.drawText(
                    label,
                    dividerX - 20f,
                    yOffset + barThicknessPx / 2 + 15f,
                    textPaintRight
                )
            }
        }
    }
}

private fun roundToSecondHighestFiveUnit(number: Int): Int {
    val magnitude = Math.pow(10.0, (number.toString().length - 1).toDouble()).toInt()
    return ((number + magnitude - 1) / magnitude) * magnitude
}


@Composable
fun StatsResultList(
    dataList: List<StatsUiState.StatsListItem>,
    type: StatsType
) {
    val average = dataList.map { it.price }.average()

    var visibleCount by remember { mutableStateOf(2) }
    val expanded = visibleCount >= dataList.size

    Column(
        Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.space_medium))
    ) {
        Text(
            text = when (type) {
                StatsType.MART -> "마트별 결과"
                StatsType.PRODUCT -> "제품별 결과"
            },
            style = TextStyle(fontSize = dimensionResource(R.dimen.sp_small).value.sp),
            color = colorResource(R.color.darker_gary)
        )
        Spacer(Modifier.height(dimensionResource(R.dimen.space_small)))

        Text(
            text = "평균 ${String.format("%.1f", average)}원",
            style = TextStyle(fontSize = dimensionResource(R.dimen.sp_large).value.sp),
            color = Color.Black,

            )

        Spacer(Modifier.height(dimensionResource(R.dimen.space_small)))
        GrayDivider()
        Spacer(Modifier.height(dimensionResource(R.dimen.space_small)))

        dataList.take(visibleCount).forEach { item ->
            Text(
                text = "${item.name} ${item.price}",
                style = TextStyle(fontSize = dimensionResource(R.dimen.sp_medium).value.sp),
                color = Color.Black
            )
        }

        Text(
            text = if (expanded) "접어보기" else "펼쳐보기",
            style = TextStyle(fontSize = dimensionResource(R.dimen.sp_small).value.sp),
            color = colorResource(R.color.darker_gary),
            modifier = Modifier
                .clickable {
                    visibleCount = if (expanded) 2 else dataList.size
                }
                .align(Alignment.End),
        )
    }
}


@Composable
fun StatsSwitchType(
    switchOtherType: () -> Unit,
    type: StatsType
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.CenterEnd
    ) {
        Row(
            modifier = Modifier
                .clickable { switchOtherType() }
        ) {
            val textStyle = TextStyle(fontSize = dimensionResource(R.dimen.sp_small).value.sp)

            Text(
                text = "마트별",
                style = textStyle,
                color = if (type == StatsType.MART) Color.Black else colorResource(R.color.darker_gary)
            )
            Text(
                text = " / ",
                style = textStyle,
                color = Color.Gray
            )
            Text(
                text = "제품별",
                style = textStyle,
                color = if (type == StatsType.PRODUCT) Color.Black else colorResource(R.color.darker_gary)
            )
        }
    }
}

@Preview
@Composable
fun StatsResultBoxPrev() {
    val uiState = StatsUiState.test()
    StatsResultBox(uiState)
}

@Preview
@Composable
fun StatsHorizontalChartPrev() {
    StatsHorizontalChart(StatsUiState.test().dataList)
}

@Preview
@Composable
fun StatsResultListPre() {
    val uiState = StatsUiState.test()
    StatsResultList(uiState.dataList, uiState.type)
}
