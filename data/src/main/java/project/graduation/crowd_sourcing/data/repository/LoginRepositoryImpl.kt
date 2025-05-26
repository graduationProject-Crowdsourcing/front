package project.graduation.crowd_sourcing.data.repository

import android.util.Log
import project.graduation.crowd_sourcing.domain.local.TokenManager
import project.graduation.crowd_sourcing.data.mapper.login.toEntity
import project.graduation.crowd_sourcing.data.request.LoginRequest
import project.graduation.crowd_sourcing.data.request.RefreshTokenRequest
import project.graduation.crowd_sourcing.data.request.SignUpRequest
import project.graduation.crowd_sourcing.data.service.LoginService
import project.graduation.crowd_sourcing.domain.model.entity.login.LoginEntity
import project.graduation.crowd_sourcing.domain.repository.LoginRepository
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val loginService: LoginService,
    private val tokenManager: TokenManager
) : LoginRepository {

    override suspend fun login(username: String, password: String): Result<LoginEntity> {
        return try {
            val response = loginService.login(LoginRequest(username, password))

            // 1. 전체 헤더 로그
            Log.d("LoginDebug", "Headers: ${response.headers()}")

            // 2. Authorization 헤더에서 accessToken 추출
            val accessToken = response.headers()["authorization"]?.removePrefix("Bearer ")?.trim()

            // 3. Set-Cookie에서 refreshToken 추출
            val refreshToken = response.headers().toMultimap()["Set-Cookie"]
                ?.firstOrNull { it.startsWith("refreshToken=") }
                ?.substringAfter("refreshToken=")
                ?.substringBefore(";")

            // 4. 추출 결과 로그 확인
            Log.d("LoginDebug", "Extracted AccessToken: $accessToken")
            Log.d("LoginDebug", "Extracted RefreshToken: $refreshToken")

            val loginResponse = response.body()
            Log.d("LoginDebug", "LoginResponse: $loginResponse")

            if (response.isSuccessful && !accessToken.isNullOrBlank() && loginResponse != null) {
                tokenManager.save(
                    accessToken = accessToken,
                    refreshToken = refreshToken ?: "",
                    userId = loginResponse.data.id
                )
                Result.success(loginResponse.toEntity())
            } else {
                Result.failure(Exception("로그인 실패: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signUp(username: String, password: String, nickname: String): Result<String> {
        return try {
            val request = SignUpRequest(username, password, nickname)
            val response = loginService.signUp(request)
            Result.success(response.message)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout(accessToken: String): Result<Unit> {
        return try {
            val response = loginService.logout("Bearer $accessToken")
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("로그아웃 실패: ${response.code()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun refreshToken(refreshToken: String): Result<Pair<String, String>> {
        return try {
            val request = RefreshTokenRequest(refreshToken)
            val response = loginService.refreshToken(request)

            if (response.isSuccessful) {
                val body = response.body()!!
                Result.success(body.data.accessToken to body.data.refreshToken)
            } else {
                Result.failure(Exception("토큰 재발급 실패"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
