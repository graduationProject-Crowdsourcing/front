package project.graduation.crowd_sourcing.data.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import project.graduation.crowd_sourcing.data.mapper.worker.toEntity
import project.graduation.crowd_sourcing.data.request.worker.PostAssignmentRequest
import project.graduation.crowd_sourcing.data.request.worker.PostWorkRequest
import project.graduation.crowd_sourcing.data.response.worker.WorkCountEntity
import project.graduation.crowd_sourcing.data.response.worker.WorkHistoryEntity
import project.graduation.crowd_sourcing.data.response.worker.WorkHourEntity
import project.graduation.crowd_sourcing.data.response.worker.WorkMostEntity
import project.graduation.crowd_sourcing.data.response.worker.WorkPointEntity
import project.graduation.crowd_sourcing.data.service.WorkerService
import project.graduation.crowd_sourcing.domain.model.WorkStatus
import project.graduation.crowd_sourcing.domain.repository.WorkerRepository
import javax.inject.Inject

class WorkerRepositoryImpl @Inject constructor(
    private val workerService: WorkerService
) : WorkerRepository {
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun postWork(
        work: String,
        workCount: Int,
        workpoint: Int,
        martNames: List<String>,
        sigungu: String,
        item: String,
        workDate: String,
        memberId: Int,
        category: String,
        workhour: Int,
        expirationDate: String
    ): Result<Int> {
        return try {
            val request = PostWorkRequest(
                work = work,
                workCount = workCount,
                workpoint = workpoint,
                martNames = martNames,
                sigungu = sigungu,
                item = item,
                workDate = workDate,
                memberId = memberId,
                category = category,
                workhour = workhour,
                expirationDate = expirationDate
            )
            val response = workerService.postWorker(request)
            Log.d("SubmitRequest", "📡 응답 코드: ${response.code()}")
            Log.d("SubmitRequest", "📡 응답 바디: ${response.body()}")
            Log.d("SubmitRequest", "📡 에러 바디: ${response.errorBody()?.string()}")

            val body = response.body() ?: throw Exception("응답 바디가 비어 있습니다.")
            if (body.isNotEmpty()) {
                Result.success(body[0])
            } else {
                throw Exception("응답 배열이 비어 있습니다.")
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override suspend fun postAssignment(
        item: String,
        itemPrice: Int,
        workDate: String,
        martName: String,
        assignmentId: Int,
        username: String
    ): Result<Unit> {
        return try{
            workerService.postAssignment(
                request = PostAssignmentRequest(
                    item = item,
                    itemPrice = itemPrice,
                    workDate = workDate,
                    martName = martName
                ),
                assignmentId = assignmentId,
                username = username
            )

            Result.success(Unit)
        }catch (e:Exception){
            Result.failure(e)
        }
    }


    override suspend fun getWorkerCounts(username: String): Result<WorkCountEntity> {
        return try {
            val response = workerService.getWorkerCounts(username).toEntity()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getWorkerPoint(username: String): Result<WorkPointEntity> {
        return try {
            val response = workerService.getWorkerPoint(username).toEntity()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getWorking(username: String): Result<List<WorkHistoryEntity>> {
        return try {
            val response = workerService.getWorking(username).map { it.toEntity() }
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getWorkerHour(username: String): Result<WorkHourEntity> {
        return try {
            val response = workerService.getWorkerHour(username).toEntity()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getWorkHistory(
        username: String,
        status: WorkStatus
    ): Result<List<WorkHistoryEntity>> {
        return try {
            val response =
                workerService.getWorkHistory(username, status.toString()).map { it.toEntity() }
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getWorkMost(username: String): Result<WorkMostEntity> {
        return try {
            val response = workerService.getWorkMost(username).toEntity()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}