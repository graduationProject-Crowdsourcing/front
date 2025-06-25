package project.graduation.crowd_sourcing.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import project.graduation.crowd_sourcing.data.mapper.formatToString
import project.graduation.crowd_sourcing.data.mapper.worker.toEntity
import project.graduation.crowd_sourcing.data.request.worker.PostWorkRequest
import project.graduation.crowd_sourcing.data.response.worker.WorkCountEntity
import project.graduation.crowd_sourcing.data.response.worker.WorkHistoryEntity
import project.graduation.crowd_sourcing.data.response.worker.WorkHourEntity
import project.graduation.crowd_sourcing.data.response.worker.WorkMostEntity
import project.graduation.crowd_sourcing.data.response.worker.WorkPointEntity
import project.graduation.crowd_sourcing.data.service.WorkerService
import project.graduation.crowd_sourcing.domain.model.Region
import project.graduation.crowd_sourcing.domain.model.WorkStatus
import project.graduation.crowd_sourcing.domain.repository.WorkerRepository
import java.time.LocalDateTime
import javax.inject.Inject

class WorkerRepositoryImpl @Inject constructor(
    private val workerService: WorkerService
) : WorkerRepository {
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun postWork(
        work: String,
        workCount: Int,
        workpoint: Int,
        martName: String,
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
                martName = martName,
                sigungu = sigungu,
                item = item,
                workDate = workDate,
                memberId = memberId,
                category = category,
                workhour = workhour,
                expirationDate = expirationDate
            )
            val response = workerService.postWorker(request)
            Result.success(response)
        } catch (e: Exception) {
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