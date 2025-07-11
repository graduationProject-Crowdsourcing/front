package project.graduation.crowd_sourcing.data.response.my

import java.time.LocalDateTime


data class RecentCommissionResponse(
    val assignmentId: Int,
    val workId: Int,
    val workName: String,
    val workStatus: String,
    val region: String,
    val category:String,
    val createdAt: LocalDateTime
)
