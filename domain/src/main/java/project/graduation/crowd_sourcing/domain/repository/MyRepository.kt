package project.graduation.crowd_sourcing.domain.repository

import android.net.Uri
import project.graduation.crowd_sourcing.domain.model.entity.my.RecentCommissionEntity
import project.graduation.crowd_sourcing.domain.model.entity.my.RecentWorkEntity
import retrofit2.Response

interface MyRepository {
    suspend fun getRecentWork(): Result<RecentWorkEntity>

    suspend fun getRecentCommission(): Result<RecentCommissionEntity>

    suspend fun putNickname(nickname: String): Result<Unit>

    suspend fun postProfileImage(imageUri: Uri): Result<String>
}