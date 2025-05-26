package project.graduation.crowd_sourcing.domain.model.entity.login

data class LoginEntity(
    val id: Int,
    val accessToken: String,
    val refreshToken: String
)