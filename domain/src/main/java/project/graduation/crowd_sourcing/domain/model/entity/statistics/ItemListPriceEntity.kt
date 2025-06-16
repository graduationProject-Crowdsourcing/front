package project.graduation.crowd_sourcing.domain.model.entity.statistics

import project.graduation.crowd_sourcing.domain.model.Region


data class ItemListPriceEntity(
    val region: Region,
    val category: String,
    val item: String,
    val averagePrice: Int
)
