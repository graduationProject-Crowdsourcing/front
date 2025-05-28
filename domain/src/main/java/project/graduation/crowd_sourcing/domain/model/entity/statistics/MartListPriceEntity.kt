package project.graduation.crowd_sourcing.domain.model.entity.statistics

import project.graduation.crowd_sourcing.domain.model.Region

data class MartListPriceEntity(
    val region: Region,
    val category: String,
    val mart: String,
    val categoryPrice: Int
)
