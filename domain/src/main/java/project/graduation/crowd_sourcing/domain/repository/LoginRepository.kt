package project.graduation.crowd_sourcing.domain.repository

import project.graduation.crowd_sourcing.domain.model.entity.login.LoginEntity

interface LoginRepository {
    suspend fun login(username: String, password: String): Result<LoginEntity>
}