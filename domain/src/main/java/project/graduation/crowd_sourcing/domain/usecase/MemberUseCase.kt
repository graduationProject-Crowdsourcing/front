package project.graduation.crowd_sourcing.domain.usecase

import project.graduation.crowd_sourcing.domain.local.TokenManager
import project.graduation.crowd_sourcing.domain.model.entity.login.LoginEntity
import project.graduation.crowd_sourcing.domain.repository.LoginRepository
import javax.inject.Inject

class MemberUseCase @Inject constructor(
    private val repository: LoginRepository,
    private val tokenManager: TokenManager
) {
    // 로그인
    suspend fun login(username: String, password: String): Result<LoginEntity> {
        tokenManager.saveUsername(username)
        return repository.login(username, password)
    }

    // 로그아웃
    suspend fun logout(accessToken: String): Result<Unit> {
        return repository.logout(accessToken)
    }

    // 회원가입
    suspend fun signUp(username: String, password: String, nickname: String): Result<String> {
        return repository.signUp(username, password, nickname)
    }

    // 토큰 재발급
    suspend fun refreshToken(refreshToken: String): Result<Pair<String, String>> {
        return repository.refreshToken(refreshToken)
    }
}
