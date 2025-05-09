package project.graduation.crowd_sourcing.domain.model.entity

import java.time.LocalDateTime

data class Commission(
    val commission: String,
    val commissionPoint: Int,
    val deadline: LocalDateTime
)
