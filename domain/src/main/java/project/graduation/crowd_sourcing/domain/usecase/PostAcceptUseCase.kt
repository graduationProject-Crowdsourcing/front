package project.graduation.crowd_sourcing.domain.usecase

import project.graduation.crowd_sourcing.domain.repository.AlarmRepository
import project.graduation.crowd_sourcing.domain.repository.FcmRepository
import javax.inject.Inject

class PostAcceptUseCase @Inject constructor(
    private val alarmRepository: AlarmRepository,
    private val fcmRepository: FcmRepository
) {
    suspend operator fun invoke(workId: Int, memberId: Int): Result<Unit> {
//        return alarmRepository.postAccept(workId, memberId)
        return fcmRepository.postAccept(workId = workId, memberId = memberId)
    }
} 