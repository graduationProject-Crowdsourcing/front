package project.graduation.crowd_sourcing.domain.repository

import project.graduation.crowd_sourcing.data.response.worker.WorkCountEntity
import project.graduation.crowd_sourcing.data.response.worker.WorkHistoryEntity
import project.graduation.crowd_sourcing.data.response.worker.WorkHourEntity
import project.graduation.crowd_sourcing.data.response.worker.WorkMostEntity
import project.graduation.crowd_sourcing.data.response.worker.WorkPointEntity
import project.graduation.crowd_sourcing.domain.model.Region
import project.graduation.crowd_sourcing.domain.model.WorkStatus
import java.time.LocalDateTime

interface WorkerRepository {
    suspend fun postWork(
        work: String,
        workCount: Int,
        workPoint: Int,
        region: Region,
        item: String,
        itemPrice: Int,
        workDate: LocalDateTime,
        memberId: Int
    ): Result<Int>

    suspend fun getWorkerCounts(userId: String): Result<WorkCountEntity>

    suspend fun getWorkerPoint(userId: String): Result<WorkPointEntity>

    suspend fun getWorking(userId: String): Result<List<WorkHistoryEntity>>

    suspend fun getWorkerHour(userId: String): Result<WorkHourEntity>

    suspend fun getWorkHistory(
        userId: String,
        status: WorkStatus // COMPLETED, CANCEL
    ): Result<List<WorkHistoryEntity>>

    suspend fun getWorkMost(userId: String): Result<WorkMostEntity>
}