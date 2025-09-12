package project.graduation.crowd_sourcing.domain.model.entity.statistics

data class ItemMinMaxPriceEntity(
    val region: String,
    val category: String,
    val item: String,
    val itemPrice: Int,
    val mart: String,
    val averagePrice: Int,
    val difference: Int
)
