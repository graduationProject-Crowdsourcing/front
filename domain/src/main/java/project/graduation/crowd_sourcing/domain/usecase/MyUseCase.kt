package project.graduation.crowd_sourcing.domain.usecase

import android.net.Uri
import project.graduation.crowd_sourcing.domain.model.entity.my.RecentCommissionEntity
import project.graduation.crowd_sourcing.domain.model.entity.my.RecentWorkEntity
import project.graduation.crowd_sourcing.domain.repository.MyRepository
import javax.inject.Inject

class MyUseCase @Inject constructor(
    private val repository: MyRepository
) {
    suspend fun getRecentHistory(): Result<Pair<RecentWorkEntity, RecentCommissionEntity>> {
        val recentWorkResult = repository.getRecentWork()
        val recentCommissionResult = repository.getRecentCommission()

        return if (recentWorkResult.isSuccess && recentCommissionResult.isSuccess) {
            val recentWork = recentWorkResult.getOrThrow()
            val recentCommission = recentCommissionResult.getOrThrow()
            Result.success(Pair(recentWork, recentCommission))
        } else {
            val exception =
                recentWorkResult.exceptionOrNull() ?: recentCommissionResult.exceptionOrNull()
            Result.failure(exception ?: Exception("알 수 없는 오류"))
        }
    }

    suspend fun putNickname(nickname: String): Result<Unit> {
        return repository.putNickname(nickname)
    }

    suspend fun changeMyProfileImage(imgUri: Uri): Result<String> {
        return try {
            repository.postProfileImage(imgUri)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
