package project.graduation.crowd_sourcing.domain.model.entity.my

import java.time.LocalDateTime

data class RecentWorkEntity(
    val id: Int,
    val work: String,
    val region: String,
    val category: String,
    val item: String,
    val itemPrice: Int,
    val workDate: LocalDateTime
)