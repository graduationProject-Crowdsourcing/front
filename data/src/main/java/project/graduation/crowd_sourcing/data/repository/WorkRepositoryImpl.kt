package project.graduation.crowd_sourcing.data.repository

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import project.graduation.crowd_sourcing.data.service.WorkService
import project.graduation.crowd_sourcing.domain.repository.WorkRepository
import retrofit2.HttpException
import java.io.File
import java.io.IOException
import javax.inject.Inject

class WorkRepositoryImpl @Inject constructor(
    private val service: WorkService,
    @ApplicationContext private val context: Context
) : WorkRepository {

    override suspend fun uploadImage(
        username: String,
        fileName: String,
        uri: Uri
    ): Result<Unit> {
        return try {
            val directoryPath = "images/$fileName"
            service.uploadImage(
                username,
                directoryPath,
                compressAndResizeImage(context, uri, fileName)
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun requestOcr(
        fileName: String,
        commissionId: String
    ): Result<String> {
        return try {
            val response = service.requestOcr(fileName, commissionId)
            if (response.isSuccessful) {
                Result.success(response.body() ?: "")
            } else {
                Result.failure(HttpException(response))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
