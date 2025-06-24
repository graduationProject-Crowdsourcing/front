package project.graduation.crowd_sourcing.domain.usecase

import project.graduation.crowd_sourcing.data.response.worker.WorkHistoryEntity
import project.graduation.crowd_sourcing.domain.local.TokenManager
import project.graduation.crowd_sourcing.domain.repository.WorkerRepository
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class WorkerUseCase @Inject constructor(
    private val workerRepository: WorkerRepository,
    private val tokenManager: TokenManager
) {
    suspend fun getWorkOngoingList(): Result<List<WorkHistoryEntity>> {
        return try {
            tokenManager.getUserName()?.let { username ->
                workerRepository.getWorking(username)
            } ?: Result.failure(IllegalStateException("로그인된 사용자 정보가 없습니다."))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun postAssignment(
        item: String,
        itemPrice: Int,
        workDate: LocalDateTime,
        martName: String,
        assignmentId: Int
    ): Result<Unit>{
        return tokenManager.getUserName()?.let{username->
            val isoString = workDate.format(DateTimeFormatter.ISO_DATE_TIME)


            workerRepository.postAssignment(
                item = item,
                itemPrice = itemPrice,
                workDate = isoString,
                martName = martName,
                assignmentId = assignmentId,
                username = username
            )
        } ?: Result.failure(IllegalStateException("로그인된 사용자 정보가 없습니다."))
    }
}