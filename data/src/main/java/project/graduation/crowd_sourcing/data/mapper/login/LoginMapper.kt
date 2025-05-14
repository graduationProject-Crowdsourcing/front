package project.graduation.crowd_sourcing.data.mapper.login

import project.graduation.crowd_sourcing.data.response.login.LoginResponse
import project.graduation.crowd_sourcing.domain.model.entity.login.LoginEntity

fun LoginResponse.toEntity(): LoginEntity {
    return LoginEntity(
        id = this.data.id,
        accessToken = this.data.accessToken,
        refreshToken = this.data.refreshToken
    )
}