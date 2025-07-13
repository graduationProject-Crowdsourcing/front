package project.graduation.crowd_sourcing.data.service

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface WorkService {

    @Multipart
    @POST("/api/v1/request/api/image/test")
    suspend fun uploadImage(
        @Query("username") username: String,
        @Query("directoryPath") directoryPath: String,
        @Part image: MultipartBody.Part
    ):  Response<ResponseBody>?

    @GET("/api/v1/request/naverOcr")
    suspend fun requestOcr(
        @Query("fileName") fileName: String,
        @Query("commission") commissionId: String
    ): Response<ResponseBody>

}
