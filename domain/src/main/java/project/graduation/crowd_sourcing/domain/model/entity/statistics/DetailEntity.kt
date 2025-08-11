package project.graduation.crowd_sourcing.domain.model.entity.statistics

import java.time.LocalDateTime

data class DetailEntity(
    val commission: String,
    val commissionregion: String,
    val category: String,
    val commissionDate: LocalDateTime,
    val expirationDate: LocalDateTime,
    val commisionCount: Int,
    val commisionpoint: Int
)
