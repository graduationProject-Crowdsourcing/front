package project.graduation.crowd_sourcing.presentation.ui.screen.stats

import java.util.Date

data class StatsUiState (
    val requestRegion: String,
    val requestStartDate: Date,
    val requestCompleteDate: Date,
    val requestProduct: String,
    val martList: List<MartData>,

    ){
    data class MartData(
        val name: String,
        val price: Int
    )
}