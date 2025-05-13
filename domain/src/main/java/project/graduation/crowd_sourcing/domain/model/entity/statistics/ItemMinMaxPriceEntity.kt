package project.graduation.crowd_sourcing.domain.model.entity.statistics

import project.graduation.crowd_sourcing.domain.model.Region

data class ItemMinMaxPriceEntity(
    val region: Region,
    val category: String,
    val item: String,
    val itemPrice: Int,
    val mart: String,
    val averagePrice: Int,
    val difference: Int
)
