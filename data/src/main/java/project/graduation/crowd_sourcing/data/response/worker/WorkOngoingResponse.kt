package project.graduation.crowd_sourcing.data.response.worker

import java.time.LocalDateTime

data class WorkOngoingResponse(
    val id: Int,
    val workAssignmentId: Int,
    val commission: String,
    val commissionCount: Int,
    val commissionPoint: Int,
    val commissionRegion: String,
    val commissionDate: LocalDateTime,
    val commissionStatus: String,
    val memberId: Int
)
