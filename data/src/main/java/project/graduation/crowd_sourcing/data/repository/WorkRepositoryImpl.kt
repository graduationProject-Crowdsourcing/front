package project.graduation.crowd_sourcing.data.repository

import android.content.Context
import android.net.Uri
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import project.graduation.crowd_sourcing.data.service.WorkService
import project.graduation.crowd_sourcing.domain.repository.WorkRepository
import retrofit2.HttpException
import javax.inject.Inject
import java.lang.reflect.Type


class WorkRepositoryImpl @Inject constructor(
    private val service: WorkService,
    @ApplicationContext private val context: Context
) : WorkRepository {

    override suspend fun uploadImage(
        username: String,
        fileName: String,
        uri: Uri
    ): Result<String> {
        return try {
            val directoryPath = "images/$fileName"
            val response = service.uploadImage(
                username,
                directoryPath,
                compressAndResizeImage(context, uri, fileName)
            )

            val responseString = response?.body()?.string() ?: ""

            Result.success(responseString)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun requestOcr(
        fileName: String,
        commissionId: String
    ): Result<List<String>> {
        return try {
            val response = service.requestOcr(fileName, commissionId)

            if (response.isSuccessful) {
                val rawString = response.body()?.string() ?: "[]"

                val listType = object : TypeToken<List<String>>() {}.type
                val parsedList: List<String> = Gson().fromJson(rawString, listType)

                Result.success(parsedList)
            } else {
                Result.failure(HttpException(response))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}
