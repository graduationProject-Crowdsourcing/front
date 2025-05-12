package project.graduation.crowd_sourcing.domain.model.entity.userpoint

import project.graduation.crowd_sourcing.domain.model.Region
import java.time.LocalDateTime

data class UserPointHistoryEntity(
    val type: String,
    val region: Region,
    val item: String,
    val point: Int,
    val date: LocalDateTime
)