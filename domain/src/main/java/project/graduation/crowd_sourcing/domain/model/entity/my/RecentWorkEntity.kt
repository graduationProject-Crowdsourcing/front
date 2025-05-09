package project.graduation.crowd_sourcing.domain.model.entity.my

import project.graduation.crowd_sourcing.domain.model.Region
import java.util.Date

data class RecentWorkEntity(
    val id: Int,
    val work: String,
    val region: Region,
    val category: String,
    val item: String,
    val itemPrice: Int,
    val workDate: Date
)