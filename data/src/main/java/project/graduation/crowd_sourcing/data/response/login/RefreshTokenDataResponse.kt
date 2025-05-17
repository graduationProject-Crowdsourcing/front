package project.graduation.crowd_sourcing.data.response.login

data class RefreshTokenDataResponse(
    val accessToken: String,
    val refreshToken: String
)