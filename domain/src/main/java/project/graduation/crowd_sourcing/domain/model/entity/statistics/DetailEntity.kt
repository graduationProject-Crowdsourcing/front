package project.graduation.crowd_sourcing.domain.model.entity.statistics

import project.graduation.crowd_sourcing.domain.model.Region
import java.time.LocalDateTime

data class DetailEntity(
    val commission: String,
    val commissionregion: Region,
    val category: String,
    val commissionDate: LocalDateTime,
    val commisionCount: Int,
    val commisionpoint: Int
)
