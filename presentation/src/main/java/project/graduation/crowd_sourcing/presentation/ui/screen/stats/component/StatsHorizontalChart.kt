package project.graduation.crowd_sourcing.presentation.ui.screen.stats.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.xr.compose.testing.toDp
import project.graduation.crowd_sourcing.presentation.R
import project.graduation.crowd_sourcing.presentation.ui.screen.stats.StatsUiState

@Composable
fun StatsHorizontalChart() {
    val chartList = listOf(
        StatsUiState.MartData(name = "Apple Juice", price = 200),
        StatsUiState.MartData(name = "Banana Smoothie", price = 150),
        StatsUiState.MartData(name = "Cherry Pie", price = 100),
        StatsUiState.MartData(name = "Date Cookies", price = 280)
    )

    // data set
    val labels = chartList.map { it.name }

    val mostExpensivePrice = roundToSecondHighestFiveUnit(chartList.maxOf { it.price })
    val barWidthsRatio = chartList.map { it.price.toFloat() / mostExpensivePrice }

    // dp
    val barThicknessDp = 28.dp
    val barSpacingDp = 20.dp
    val labelSp = dimensionResource(R.dimen.sp_small)

    // dp to px
    val density = LocalDensity.current
    val barThicknessPx = with(density) { barThicknessDp.toPx() }
    val barSpacingPx = with(density) { barSpacingDp.toPx() }
    val textPaint = android.graphics.Paint().apply {
        color = colorResource(R.color.darker_gary).toArgb()
        textSize = with(density) { labelSp.toPx() }
        textAlign = android.graphics.Paint.Align.RIGHT
    }

    val barColor = colorResource(R.color.primary)
    val dividerColor = colorResource(R.color.gray)

    val chartHeightDp = (chartList.size * (barThicknessPx + barSpacingPx) - barSpacingPx).toDp()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = colorResource(R.color.gray),
                shape = RoundedCornerShape(dimensionResource(R.dimen.round_common))
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(dimensionResource(R.dimen.round_common))
                .height(chartHeightDp)
        ) {
            val dividerX = 160f

            val numLines = 5
            val spacing = (size.width - dividerX) / (numLines - 1)
            for (i in 0 until numLines) {
                val currentX = dividerX + (i * spacing)
                drawLine(
                    color = dividerColor,
                    start = Offset(currentX, 0f),
                    end = Offset(currentX, size.height),
                    strokeWidth = if (i == 0) 4f else 2f
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

                val splitLabel = labels[index].split(" ")
                splitLabel.forEachIndexed { lineIndex, lineText ->
                    val totalTextHeight = (splitLabel.size - 1) * 45f
                    val textBlockCenterOffset = totalTextHeight / 2

                    drawContext.canvas.nativeCanvas.drawText(
                        lineText,
                        dividerX - 20f,
                        yOffset + barThicknessPx / 2 - textBlockCenterOffset + (lineIndex * 45f) + 15f, // 중앙에 배치되도록 위치 조정
                        textPaint
                    )
                }
            }
        }
    }
}

private fun roundToSecondHighestFiveUnit(number: Int): Int {
    val magnitude = Math.pow(10.0, (number.toString().length - 1).toDouble()).toInt()
    return ((number + magnitude - 1) / magnitude) * magnitude
}


@Preview
@Composable
fun StatsHorizontalChartPrev() {
    StatsHorizontalChart()
}