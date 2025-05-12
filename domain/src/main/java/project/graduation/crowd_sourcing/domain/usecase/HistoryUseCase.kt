package project.graduation.crowd_sourcing.domain.usecase

import project.graduation.crowd_sourcing.domain.model.entity.userpoint.UserPointHistoryEntity
import project.graduation.crowd_sourcing.domain.repository.UserPointRepository
import javax.inject.Inject

class HistoryUseCase @Inject constructor(
    private val userPointRepository: UserPointRepository
) {
    suspend fun getUsePointHistory(memberId: Int): Result<List<UserPointHistoryEntity>> {
        return userPointRepository.getUserPointHistory(memberId)
    }
    fun getUserCommissionHistory() {}
    fun getUserRequestHistory() {}
}