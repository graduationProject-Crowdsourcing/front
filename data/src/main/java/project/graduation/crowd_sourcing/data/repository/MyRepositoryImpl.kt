package project.graduation.crowd_sourcing.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import project.graduation.crowd_sourcing.data.mapper.my.toEntity
import project.graduation.crowd_sourcing.data.request.MyNicknameRequest
import project.graduation.crowd_sourcing.data.service.MyService
import project.graduation.crowd_sourcing.domain.local.TokenManager
import project.graduation.crowd_sourcing.domain.model.entity.my.ProfileEntity
import project.graduation.crowd_sourcing.domain.model.entity.my.RecentCommissionEntity
import project.graduation.crowd_sourcing.domain.model.entity.my.RecentWorkEntity
import project.graduation.crowd_sourcing.domain.repository.MyRepository
import retrofit2.Response
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class MyRepositoryImpl @Inject constructor(
    private val myService: MyService,
    private val tokenManager: TokenManager,
    @ApplicationContext private val context: Context
) : MyRepository {
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getRecentWork(): Result<RecentWorkEntity> {
        val userId = tokenManager.getUserId()
        return try {
            val response = myService.getRecentWork(userId)
            Result.success(response.toEntity())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getRecentCommission(): Result<RecentCommissionEntity> {
        val userId = tokenManager.getUserId()
        return try {
            val response = myService.getRecentCommission(userId)
            Result.success(response.toEntity())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun putNickname(nickname: String): Result<Unit> {
        val userId = tokenManager.getUserId()
        return try {
            myService.putNickname(
                userId,
                request = MyNicknameRequest(nickname)
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override suspend fun postProfileImage(imagUri: Uri): Result<String> {
        val username = tokenManager.getUserName()
            ?: return Result.failure(IllegalStateException("No username found"))

        return try {
            val file = compressAndResizeImage(context, imagUri)
            val response = myService.postProfileImage(username, file)

            if (response.isSuccessful) {
                val imageUrl = response.body()?.data?.imageUrl
                if (imageUrl != null) Result.success(imageUrl)
                else Result.failure(NullPointerException("imageUrl is null"))
            } else {
                Result.failure(Exception("API error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getProfile(): Result<ProfileEntity> {
        val username = tokenManager.getUserName()
            ?: return Result.failure(IllegalStateException("No username found"))

        return try {
            val response = myService.getProfile(username)
            val entity = response.body()?.toEntity()
                ?: return Result.failure(NullPointerException("Response body is null"))
            Result.success(entity)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override suspend fun getProfileImg(): Result<String> {
        val username = tokenManager.getUserName()
            ?: return Result.failure(IllegalStateException("No username found"))
        return try {
            val response = myService.getProfileImg(username)
            val url = response.body()?.data?.imageUrl
                ?: return Result.failure(NullPointerException("Response body is null"))
            Result.success(url)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


}


internal fun compressAndResizeImage(context: Context, uri: Uri): MultipartBody.Part {
    val maxWidth = 1024
    val maxHeight = 1024

    val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        val source = ImageDecoder.createSource(context.contentResolver, uri)
        ImageDecoder.decodeBitmap(source)
    } else {
        MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
    }

    val ratio = minOf(
        maxWidth / bitmap.width.toFloat(),
        maxHeight / bitmap.height.toFloat(),
        1f
    )
    val resized = Bitmap.createScaledBitmap(
        bitmap,
        (bitmap.width * ratio).toInt(),
        (bitmap.height * ratio).toInt(),
        true
    )

    val outputStream = ByteArrayOutputStream()
    resized.compress(Bitmap.CompressFormat.JPEG, 75, outputStream)
    val byteArray = outputStream.toByteArray()

    val requestBody = byteArray.toRequestBody("image/jpeg".toMediaTypeOrNull())
    return MultipartBody.Part.createFormData("file", "compressed.jpg", requestBody)
}

