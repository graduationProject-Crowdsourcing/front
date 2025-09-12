package project.graduation.crowd_sourcing.domain.model.entity.my

import java.time.LocalDateTime

data class RecentCommissionEntity(
    val id: Int,
    val commission: String,
    val region:String,
    val category:String,
    val createdAt: LocalDateTime
)