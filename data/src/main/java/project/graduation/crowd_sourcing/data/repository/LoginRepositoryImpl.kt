package project.graduation.crowd_sourcing.data.repository

import project.graduation.crowd_sourcing.data.mapper.login.toEntity
import project.graduation.crowd_sourcing.data.request.LoginRequest
import project.graduation.crowd_sourcing.data.service.LoginService
import project.graduation.crowd_sourcing.domain.model.entity.login.LoginEntity
import project.graduation.crowd_sourcing.domain.repository.LoginRepository
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val loginService: LoginService
) : LoginRepository {

    override suspend fun login(username: String, password: String): Result<LoginEntity> {
        return try {
            val request = LoginRequest(username, password)
            val response = loginService.login(request)
            Result.success(response.toEntity())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
