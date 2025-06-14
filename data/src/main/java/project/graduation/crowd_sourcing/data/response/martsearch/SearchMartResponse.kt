package project.graduation.crowd_sourcing.data.response.martsearch

data class ApiResponseDtoListMartDto (
    val status: Int,
    val message: String,
    val data: List<MartDto>
)

data class MartDto(
    val martId: Int,
    val martName: String,
    val latitude: Double,
    val longitude: Double,
    val sido: String,
    val sigungu: String?,
    val dong: String?,
    val existCommission: Int
)

data class MartDto1(
    val sigungu: String,
    val martName: String,
    val lat: Double,
    val lng: Double
)