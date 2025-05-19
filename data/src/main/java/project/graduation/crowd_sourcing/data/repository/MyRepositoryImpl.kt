package project.graduation.crowd_sourcing.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import project.graduation.crowd_sourcing.data.mapper.my.toEntity
import project.graduation.crowd_sourcing.data.service.MyService
import project.graduation.crowd_sourcing.domain.model.entity.my.RecentCommissionEntity
import project.graduation.crowd_sourcing.domain.model.entity.my.RecentWorkEntity
import project.graduation.crowd_sourcing.domain.repository.MyRepository
import javax.inject.Inject

class MyRepositoryImpl @Inject constructor(
    private val myService: MyService
) : MyRepository {
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getRecentWork(userId: Int): Result<RecentWorkEntity> {
        return try {
            val response = myService.getRecentWork(userId)
            Result.success(response.toEntity())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getRecentCommission(userId: Int): Result<RecentCommissionEntity> {
        return try {
            val response = myService.getRecentCommission(userId)
            Result.success(response.toEntity())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
