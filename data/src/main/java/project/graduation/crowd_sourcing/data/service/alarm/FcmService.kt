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
        @Query("memberId") memberId: Int,
        @Query("fcmToken") fcmToken: String
    ): Response<Unit>

    @POST("/api/v1/fcm/cancel")
    suspend fun postCancel(
        @Query("memberId") memberId: Int,
        @Query("workId") workId: Int
    ): Response<Unit>

    @POST("/api/v1/fcm/accept")
    suspend fun postAccept(
        @Query("memberId") memberId: Int,
        @Query("workId") workId: Int
    ): Response<Unit>

    @GET("/api/v1/fcm/send-notifications")
    suspend fun getSendNotifications(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double
    ): Response<Unit>
}