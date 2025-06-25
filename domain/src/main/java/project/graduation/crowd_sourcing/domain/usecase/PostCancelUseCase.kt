package project.graduation.crowd_sourcing.domain.usecase

import project.graduation.crowd_sourcing.domain.repository.AlarmRepository
import javax.inject.Inject

class PostCancelUseCase @Inject constructor(
    private val alarmRepository: AlarmRepository
) {
    suspend operator fun invoke(workId: Int, memberId: Int): Result<String> {
        return alarmRepository.postCancel(workId, memberId)
    }
} 