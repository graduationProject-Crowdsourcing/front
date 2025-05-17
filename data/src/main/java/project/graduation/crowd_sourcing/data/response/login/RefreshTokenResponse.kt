package project.graduation.crowd_sourcing.data.response.login

data class RefreshTokenResponse(
    val status: Int,
    val message: String,
    val data: RefreshTokenDataResponse
)
