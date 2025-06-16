package project.graduation.crowd_sourcing.domain.usecase

import project.graduation.crowd_sourcing.data.response.worker.WorkHistoryEntity
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
    suspend fun getUsePointHistory(): Result<List<UserPointHistoryEntity>> {
        return userPointRepository.getUserPointHistory()
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
                        countOrHour = hour,
                        point = point,
                        completed = (completedList.size).coerceAtLeast(1),
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



    suspend fun getRequest() : Result<HistoryStats>{
        return try{
            tokenManager.getUserName()?.let{ username->
                val count = requesterRepository.getRequestStats(username).commissionCount
                val point = requesterRepository.getRequestPoint(username).totalPoints
                val completedList = requesterRepository.getRequestStatus(username, WorkStatus.COMPLETED.toString())
                val currentList = requesterRepository.getOngoingRequests(username)
                val most = requesterRepository.getRequestDetail(username)

                Result.success(HistoryStats(
                    countOrHour = count,
                    point = point,
                    completed = (completedList.size).coerceAtLeast(1),
                    completedList = completedList.map {
                        WorkHistoryEntity(
                            id = it.id.toInt(),
                            commission = it.commission,
                            commissionCount = it.commissionCount,
                            commissionPoint = it.commissionPoint,
                            commissionRegion = Region.from(it.commissionRegion),
                            commissionDate = it.commissionDate,
                            commissionStatus = WorkStatus.valueOf(it.commissionStatus),
                            memberId = it.memberId
                        )
                    },
                    currentList = currentList.map {
                        WorkHistoryEntity(
                            id = it.id.toInt(),
                            commission = it.commission,
                            commissionCount = it.commissionCount,
                            commissionPoint = it.commissionPoint,
                            commissionRegion = Region.from(it.commissionRegion),
                            commissionDate = it.commissionDate,
                            commissionStatus = WorkStatus.valueOf(it.commissionStatus),
                            memberId = it.memberId
                        )
                    },
                    mostRegion = Region.from(most.mostRequestedRegion),
                    mostCategory = Category.from(most.mostRequestedCategory)
                ))
            } ?: Result.failure(IllegalStateException("로그인된 사용자 정보가 없습니다."))
        }catch (e:Exception){
            Result.failure(e)
        }
    }
}