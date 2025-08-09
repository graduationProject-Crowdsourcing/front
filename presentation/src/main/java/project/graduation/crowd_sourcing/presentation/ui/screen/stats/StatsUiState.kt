package project.graduation.crowd_sourcing.presentation.ui.screen.stats

import android.os.Build
import androidx.annotation.RequiresApi
import project.graduation.crowd_sourcing.presentation.utils.twoDaysAgo
import java.time.LocalDateTime

data class StatsUiState(
    val requestRegion: String,
    val requestStartDate: LocalDateTime,
    val requestCompleteDate: LocalDateTime,
    val requestProduct: String,
    val dataList: List<StatsListItem>,
    val type: StatsType
) {
    data class StatsListItem(
        val name: String,
        val price: Int
    )

    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        fun test() = StatsUiState(
            requestRegion = "강남구",
            requestStartDate = twoDaysAgo,
            requestCompleteDate = LocalDateTime.now(),
            requestProduct = "라면",
            dataList = listOf(
                StatsListItem(name = "a mart", price = 200),
                StatsListItem(name = "b mart", price = 150),
                StatsListItem(name = "c mart", price = 100),
                StatsListItem(name = "d mart", price = 280)
            ),
            type = StatsType.MART
        )

        fun init() = StatsUiState(
            requestRegion = "",
            requestStartDate = LocalDateTime.now(),
            requestCompleteDate = LocalDateTime.now(),
            requestProduct = "",
            dataList = emptyList(),
            type = StatsType.MART
        )
    }
}

enum class StatsType {
    MART, PRODUCT
}