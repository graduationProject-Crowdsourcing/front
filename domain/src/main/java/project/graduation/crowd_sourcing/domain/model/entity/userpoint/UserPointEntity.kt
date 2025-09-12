package project.graduation.crowd_sourcing.domain.model.entity.userpoint

import java.time.LocalDateTime

data class UserPointHistoryEntity(
    val type: String,
    val region: String,
    val item: String,
    val point: Int,
    val date: LocalDateTime
)