package project.graduation.crowd_sourcing.domain.usecase

import project.graduation.crowd_sourcing.domain.repository.FcmRepository
import javax.inject.Inject

class PostRejectWorkUseCase @Inject constructor(
    private val fcmRepository: FcmRepository
) {
    suspend operator fun invoke(workId: Int, memberId: Int): Result<Unit> {
        return fcmRepository.postRejectWork(workId, memberId)
    }
}