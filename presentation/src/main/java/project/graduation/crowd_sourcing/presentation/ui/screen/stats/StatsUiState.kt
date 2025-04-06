package project.graduation.crowd_sourcing.presentation.ui.screen.stats

import project.graduation.crowd_sourcing.presentation.utils.twoDaysAgo
import java.util.Date

data class StatsUiState (
    val requestRegion: String,
    val requestStartDate: Date,
    val requestCompleteDate: Date,
    val requestProduct: String,
    val dataList: List<StatsListItem>,
    val type: StatsType
    ){
    data class StatsListItem(
        val name: String,
        val price: Int
    )

    companion object{
        fun test() = StatsUiState(
            requestRegion = "강남구",
            requestStartDate = twoDaysAgo,
            requestCompleteDate = Date(),
            requestProduct = "라면",
            dataList = listOf(
                StatsListItem(name = "a mart", price = 200),
                StatsListItem(name = "b mart", price = 150),
                StatsListItem(name = "c mart", price = 100),
                StatsListItem(name = "d mart", price = 280)
            ),
            type = StatsType.MART
        )
    }
}

enum class StatsType{
    MART, PRODUCT
}