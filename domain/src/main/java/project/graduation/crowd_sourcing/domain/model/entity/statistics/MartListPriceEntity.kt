package project.graduation.crowd_sourcing.domain.model.entity.statistics

data class MartListPriceEntity(
    val region: String,
    val category: String,
    val mart: String,
    val categoryPrice: Int
)
