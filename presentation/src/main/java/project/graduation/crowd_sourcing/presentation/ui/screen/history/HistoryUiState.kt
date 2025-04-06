package project.graduation.crowd_sourcing.presentation.ui.screen.history

import project.graduation.crowd_sourcing.presentation.utils.twoDaysAgo
import java.util.Date

data class HistoryUiState(
    val stats: Pair<StatsType, StatsType>,
    val currentHistoryList: List<HistoryItem>,
    val totalHistoryList: List<HistoryItem>,
    val searchQuery: String = ""
) {
    data class HistoryItem(
        val product: String,
        val category: String,
        val date: Date,
        val point: Int
    )

    sealed interface StatsType {
        data object Init : StatsType
        sealed class Work : StatsType {
            data class All(
                val totalWork: Int,
                val totalTime: Int,
                val totalPoint: Int
            ) : Work()

            data class Detail(
                val mostRegion: String,
                val averageTime: Int,
                val mostCategory: String
            ) : Work()
        }

        sealed class Request : StatsType {
            data class All(
                val totalRequests: Int,
                val totalPoint: Int,
                val completedRequests: Int
            ) : Request()

            data class Detail(
                val mostRegion: String,
                val averagePoint: Int,
                val mostCategory: String
            ) : Request()
        }
    }

    companion object {
        fun getStatsList(type: StatsType): List<Pair<String, Any>> {
            return when (type) {
                is StatsType.Request.All -> listOf(
                    "총 의뢰 횟수" to type.totalRequests,
                    "총 의뢰 포인트" to type.totalPoint,
                    "의뢰 성공 횟수" to type.completedRequests
                )

                is StatsType.Request.Detail -> listOf(
                    "최다 의뢰 지역" to type.mostRegion,
                    "평균 의뢰 포인트" to type.averagePoint,
                    "최다 의뢰 카테고리" to type.mostCategory
                )

                is StatsType.Work.All -> listOf(
                    "총 작업 횟수" to type.totalWork,
                    "총 작업 기간" to type.totalTime,
                    "총 획득 포인트" to type.totalPoint
                )

                is StatsType.Work.Detail -> listOf(
                    "최다 작업 지역" to type.mostRegion,
                    "평균 작업 기간" to type.averageTime,
                    "최다 작업 카테고리" to type.mostCategory
                )

                StatsType.Init -> {
                    emptyList()
                }
            }
        }

        fun init() = HistoryUiState(
            stats = StatsType.Init to StatsType.Init,
            currentHistoryList = emptyList(),
            totalHistoryList = emptyList()
        )

        fun initTest() = HistoryUiState(
            stats = StatsType.Work.All(
                totalWork = 20,
                totalPoint = 300,
                totalTime = 2
            ) to StatsType.Work.Detail(mostRegion = "강남구", mostCategory = "라면", averageTime = 2),
            currentHistoryList = listOf(
                HistoryItem(
                    product = "product",
                    date = twoDaysAgo,
                    point = 20,
                    category = "가공식품"
                ),
                HistoryItem(
                    product = "product",
                    date = twoDaysAgo,
                    point = 20,
                    category = "가공식품"
                ),
            ),
            totalHistoryList = listOf(
                HistoryItem(
                    product = "product",
                    date = twoDaysAgo,
                    point = 20,
                    category = "가공식품"
                ), HistoryItem(
                    product = "product",
                    date = twoDaysAgo,
                    point = 20,
                    category = "가공식품"
                ), HistoryItem(
                    product = "product",
                    date = twoDaysAgo,
                    point = 20,
                    category = "가공식품"
                ), HistoryItem(
                    product = "product",
                    date = twoDaysAgo,
                    point = 20,
                    category = "가공식품"
                ),
            )
        )
    }
}

enum class HistoryType {
    WORK, REQUEST
}