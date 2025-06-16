package project.graduation.crowd_sourcing.data.response.my

data class ProfileImgResponse(
    val status: Int,
    val message: String,
    val data: ImageData
)

data class ImageData(
    val imageUrl: String
)