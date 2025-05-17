package project.graduation.crowd_sourcing.domain.model.entity.search

import java.time.LocalDateTime

data class Commission(
    val commission: String,
    val commissionpoint: Int,
    val deadline: LocalDateTime
)
