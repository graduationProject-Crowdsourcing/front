package project.graduation.crowd_sourcing.domain.usecase

import project.graduation.crowd_sourcing.domain.repository.WorkRepository
import javax.inject.Inject

class OcrRequestUseCase @Inject constructor(
    private val repository: WorkRepository
) {
    suspend operator fun invoke(
        fileName: String,
        commissionId: String
    ): Result<String> {
        return repository.requestOcr(fileName, commissionId)
    }
}
