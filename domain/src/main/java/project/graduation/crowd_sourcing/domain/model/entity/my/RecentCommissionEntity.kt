package project.graduation.crowd_sourcing.domain.model.entity.my

import project.graduation.crowd_sourcing.domain.model.Region
import java.time.LocalDateTime
import java.util.Date

data class RecentCommissionEntity(
    val id: Int,
    val commission: String,
    val region:String,
    val category:String,
    val createdAt: LocalDateTime
)