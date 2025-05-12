package project.graduation.crowd_sourcing.presentation.ui.screen.point

import project.graduation.crowd_sourcing.presentation.utils.twoDaysAgo
import java.time.LocalDateTime
import java.util.Date

data class PointUiState(
    val list: List<PointItem>
){
    data class PointItem(
        val name: String,
        val date: LocalDateTime,
        val region: String,
        val type: PointType,
        val point: Int
    )

    enum class PointType(val type: String) {
        REQUEST("요청"), WORK("작업"), REST("요청 완료 후 남은 포인트")
    }

    companion object{
        fun test() = PointUiState(
            listOf(
                PointItem(
                    name = "삼양라면",
                    date = twoDaysAgo,
                    region = "동대문구",
                    type = PointType.REQUEST,
                    point = -200
                )
            )
        )

    }
}