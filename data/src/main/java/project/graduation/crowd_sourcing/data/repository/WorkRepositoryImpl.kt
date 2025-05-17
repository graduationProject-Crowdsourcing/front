package project.graduation.crowd_sourcing.data.repository

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
    private val service: WorkService
) : WorkRepository {

    override suspend fun uploadImage(
        username: String,
        directoryPath: String,
        imageFile: File
    ): Result<String> {
        return try {
            val requestBody = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
            val part = MultipartBody.Part.createFormData("image", imageFile.name, requestBody)

            val response = service.uploadImage(username, directoryPath, part)
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
