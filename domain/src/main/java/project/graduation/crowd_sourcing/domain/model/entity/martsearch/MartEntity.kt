package project.graduation.crowd_sourcing.domain.model.entity.martsearch

data class MartEntity(
    val martId: Int,
    val martName: String,
    val latitude: Double,
    val longitude: Double,
    val sido: String?,
    val sigungu: String?,
    val dong: String?,
    val existCommission: Int
)