package project.graduation.crowd_sourcing.domain.usecase

import project.graduation.crowd_sourcing.domain.local.TokenManager
import project.graduation.crowd_sourcing.domain.model.Category
import project.graduation.crowd_sourcing.domain.model.Region
import project.graduation.crowd_sourcing.domain.model.WorkStatus
import project.graduation.crowd_sourcing.domain.model.entity.history.HistoryStats
import project.graduation.crowd_sourcing.domain.model.entity.userpoint.UserPointHistoryEntity
import project.graduation.crowd_sourcing.domain.repository.RequesterRepository
import project.graduation.crowd_sourcing.domain.repository.UserPointRepository
import project.graduation.crowd_sourcing.domain.repository.WorkerRepository
import javax.inject.Inject

class HistoryUseCase @Inject constructor(
    private val userPointRepository: UserPointRepository,
    private val requesterRepository: RequesterRepository,
    private val workerRepository: WorkerRepository,
    private val tokenManager: TokenManager
) {
    suspend fun getUsePointHistory(memberId: Int): Result<List<UserPointHistoryEntity>> {
        return userPointRepository.getUserPointHistory(memberId)
    }
    suspend fun getCommission(): Result<HistoryStats> {
        return try {
            tokenManager.getUserName()?.let { username ->
                val hour = workerRepository.getWorkerHour(username).getOrNull()?.workhour ?: 0
                val point = workerRepository.getWorkerPoint(username).getOrNull()?.totalPoints ?: 0
                val completedList = workerRepository.getWorkHistory(username, WorkStatus.COMPLETED).getOrNull() ?: emptyList()
                val currentList = workerRepository.getWorking(username).getOrNull() ?: emptyList()
                val most = workerRepository.getWorkMost(username).getOrNull()


                Result.success(
                    HistoryStats(
                        hour = hour,
                        point = point,
                        completed = completedList.size,
                        completedList = completedList,
                        currentList = currentList,
                        mostRegion = most?.region ?: Region.UNKNOWN,
                        mostCategory = Category.from(most?.item ?: ""),
                    )
                )
            } ?: Result.failure(IllegalStateException("로그인된 사용자 정보가 없습니다."))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }



    fun getUserRequestHistory() {}
}