package project.graduation.crowd_sourcing.domain.model.entity.my

import project.graduation.crowd_sourcing.domain.model.Region
import java.util.Date

data class RecentCommissionEntity(
    val id: Long,
    val commission: String,
    val region: Region,
    val category: String,
    val commissionDate: Date,
    val commissionPoint: Int
)