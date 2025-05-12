package project.graduation.crowd_sourcing.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import project.graduation.crowd_sourcing.data.mapper.userpoint.toEntity
import project.graduation.crowd_sourcing.data.service.UserPointService
import project.graduation.crowd_sourcing.domain.model.entity.userpoint.UserPointHistoryEntity
import project.graduation.crowd_sourcing.domain.repository.UserPointRepository
import javax.inject.Inject

class UserPointRepositoryImpl @Inject constructor(
    private val userPointService: UserPointService
) : UserPointRepository {
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getUserPointHistory(memberId: Int): Result<List<UserPointHistoryEntity>> {
        return try {
            val response = userPointService.getPointHistory(memberId)
            val entityList = response.map { it.toEntity() }
            Result.success(entityList)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}