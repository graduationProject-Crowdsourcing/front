package project.graduation.crowd_sourcing.domain.usecase

import project.graduation.crowd_sourcing.domain.repository.LoginRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val repository: LoginRepository
) {
    suspend operator fun invoke(
        username: String,
        password: String,
        nickname: String
    ): Result<String> {
        return repository.signUp(username, password, nickname)
    }
}