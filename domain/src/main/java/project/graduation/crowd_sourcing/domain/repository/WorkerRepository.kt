package project.graduation.crowd_sourcing.domain.repository

import project.graduation.crowd_sourcing.data.response.worker.WorkCountEntity
import project.graduation.crowd_sourcing.data.response.worker.WorkHistoryEntity
import project.graduation.crowd_sourcing.data.response.worker.WorkHourEntity
import project.graduation.crowd_sourcing.data.response.worker.WorkMostEntity
import project.graduation.crowd_sourcing.data.response.worker.WorkPointEntity
import project.graduation.crowd_sourcing.domain.model.Region
import project.graduation.crowd_sourcing.domain.model.WorkStatus
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Path
import retrofit2.http.Query
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

    suspend fun postAssignment(
        item: String,
        itemPrice: Int,
        workDate: String,
        martName: String,
        assignmentId: Int,
        username: String
    ): Result<Unit>

    suspend fun getWorkerCounts(username: String): Result<WorkCountEntity>

    suspend fun getWorkerPoint(username: String): Result<WorkPointEntity>

    suspend fun getWorking(username: String): Result<List<WorkHistoryEntity>>

    suspend fun getWorkerHour(username: String): Result<WorkHourEntity>

    suspend fun getWorkHistory(
        username: String,
        status: WorkStatus // COMPLETED, CANCEL
    ): Result<List<WorkHistoryEntity>>

    suspend fun getWorkMost(username: String): Result<WorkMostEntity>
}