package project.graduation.crowd_sourcing.data.request

data class UpdateLocationRequest(
    val memberId: Int,
    val latitude: Double,
    val longitude: Double
)