package project.graduation.crowd_sourcing.domain.model.entity.search

import java.time.LocalDateTime

data class CommissionDetailEntity(
    val commissionId: Int,
    val commission: String,
    val region: String,
    val martName: String,
    val category: String,
    val item: String,
    val commissionPoint: Int,
    val commissionCount: Int,
    val createdAt: LocalDateTime,
    val expirationDate: LocalDateTime
) 