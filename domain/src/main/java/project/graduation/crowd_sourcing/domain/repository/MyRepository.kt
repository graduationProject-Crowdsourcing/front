package project.graduation.crowd_sourcing.domain.repository

import android.net.Uri
import okhttp3.MultipartBody
import project.graduation.crowd_sourcing.domain.model.entity.my.ProfileEntity
import project.graduation.crowd_sourcing.domain.model.entity.my.RecentCommissionEntity
import project.graduation.crowd_sourcing.domain.model.entity.my.RecentWorkEntity
import retrofit2.Response
import retrofit2.http.Part
import retrofit2.http.Query

interface MyRepository {
    suspend fun getRecentWork(): Result<RecentWorkEntity>

    suspend fun getRecentCommission(): Result<RecentCommissionEntity>

    suspend fun putNickname(nickname: String): Result<Unit>

    suspend fun postProfileImage(imageUri: Uri): Result<String>

    suspend fun patchProfileImage(imageUri: Uri): Result<String>

    suspend fun getProfile(): Result<ProfileEntity>

    suspend fun getProfileImg(): Result<String>
}
