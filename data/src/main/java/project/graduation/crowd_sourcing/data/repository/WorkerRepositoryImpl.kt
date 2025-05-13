package project.graduation.crowd_sourcing.data.repository

import project.graduation.crowd_sourcing.data.mapper.dateToString
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
    override suspend fun postWork(
        work: String,
        workCount: Int,
        workPoint: Int,
        region: Region,
        item: String,
        itemPrice: Int,
        workDate: LocalDateTime,
        memberId: Int
    ): Result<Int> {
        return try {
            val response = workerService.postWorker(
                request = PostWorkRequest(
                    work = work,
                    workCount = workCount,
                    workPoint = workPoint,
                    region = region.name,
                    item = item,
                    itemPrice = itemPrice,
                    workDate = dateToString(workDate),
                    memberId = memberId
                )
            )
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }

    }

    override suspend fun getWorkerCounts(userId: String): Result<WorkCountEntity> {
        return try {
            val response = workerService.getWorkerCounts(userId).toEntity()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getWorkerPoint(userId: String): Result<WorkPointEntity> {
        return try {
            val response = workerService.getWorkerPoint(userId).toEntity()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getWorking(userId: String): Result<List<WorkHistoryEntity>> {
        return try {
            val response = workerService.getWorking(userId).map { it.toEntity() }
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getWorkerHour(userId: String): Result<WorkHourEntity> {
        return try {
            val response = workerService.getWorkerHour(userId).toEntity()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getWorkHistory(
        userId: String,
        status: WorkStatus
    ): Result<List<WorkHistoryEntity>> {
        return try {
            val response =
                workerService.getWorkHistory(userId, status.toString()).map { it.toEntity() }
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getWorkMost(userId: String): Result<WorkMostEntity> {
        return try {
            val response = workerService.getWorkMost(userId).toEntity()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}