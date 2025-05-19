package project.graduation.crowd_sourcing.domain.usecase

import project.graduation.crowd_sourcing.domain.repository.LoginRepository
import javax.inject.Inject

class RefreshTokenUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {
    suspend operator fun invoke(refreshToken: String): Result<Pair<String, String>> {
        return loginRepository.refreshToken(refreshToken)
    }
}