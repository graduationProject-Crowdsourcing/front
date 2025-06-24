package project.graduation.crowd_sourcing.domain.model.entity.martsearch

import project.graduation.crowd_sourcing.domain.model.WorkStatus
import java.time.LocalDateTime

data class MartWorkEntity(
    val id : Int,
    val work : String,
    val workCount : Int,
    val workpoint : Int,
    val workhour : Int,
    val category: String,
    val item : String,
    val workDate : LocalDateTime,
    val itemPrice : Int,
    val workStatus: WorkStatus
)
