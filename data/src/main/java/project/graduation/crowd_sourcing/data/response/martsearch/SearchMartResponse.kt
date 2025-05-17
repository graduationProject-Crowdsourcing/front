package project.graduation.crowd_sourcing.data.response.martsearch

data class ApiResponseDtoListMartDto (
    val status: Int,
    val message: String,
    val data: List<MartDto>
)

data class MartDto(
    val sigungu: String,
    val martName: String,
    val lat: Double,
    val lng: Double
)