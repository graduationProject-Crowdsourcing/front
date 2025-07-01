package project.graduation.crowd_sourcing.data.response.my

import java.time.LocalDateTime

data class RecentWorkResponse(
    val workId: Int,
    val workName: String,
    val category: String,
    val item: String,
    val itemPrice: Int,
    val workStatus: String,
    val workCount: Int,
    val workpoint: Int,
    val workhour: Int,
    val martName: String,
    val region: String,
    val workDate: LocalDateTime,
    val expirationDate: LocalDateTime,
    val createdAt: LocalDateTime,
    val accepted: Boolean
)
