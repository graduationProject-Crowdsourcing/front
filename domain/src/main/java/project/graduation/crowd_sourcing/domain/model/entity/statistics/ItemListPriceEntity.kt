package project.graduation.crowd_sourcing.domain.model.entity.statistics


data class ItemListPriceEntity(
    val region: String,
    val category: String,
    val item: String,
    val averagePrice: Int
)
