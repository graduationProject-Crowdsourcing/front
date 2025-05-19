package project.graduation.crowd_sourcing.data.response.worker

import project.graduation.crowd_sourcing.domain.model.Region
import project.graduation.crowd_sourcing.domain.model.WorkStatus
import java.time.LocalDateTime

data class WorkHistoryEntity(
    val id: Int,
    val commission: String,
    val commissionCount: Int,
    val commissionPoint: Int,
    val commissionRegion: Region,
    val commissionDate: LocalDateTime,
    val commissionStatus: WorkStatus,
    val memberId: Int
)