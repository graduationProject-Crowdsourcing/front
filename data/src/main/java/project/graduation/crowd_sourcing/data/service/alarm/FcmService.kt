package project.graduation.crowd_sourcing.data.service.alarm

import project.graduation.crowd_sourcing.data.request.fcm.FcmRegisterRequest
import project.graduation.crowd_sourcing.data.request.fcm.FcmSendRequest
import project.graduation.crowd_sourcing.data.request.fcm.FcmWorkRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface FcmService {
    @POST("/api/v1/fcm/send")
    suspend fun poseSend(
        @Body request: FcmSendRequest
    ): Response<Unit>

    @POST("/api/v1/fcm/register")
    suspend fun postRegister(
        @Body request: FcmRegisterRequest
    ): Response<Unit>


    @POST("/api/v1/fcm/cancel")
    suspend fun postCancel(
        @Body request: FcmWorkRequest
    ): Response<Unit>

    @POST("/api/v1/fcm/accept")
    suspend fun postAccept(
        @Body request: FcmWorkRequest
    ): Response<Unit>

    @GET("/api/v1/fcm/send-notifications")
    suspend fun getSendNotifications(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double
    ): Response<Unit>
}