package project.graduation.crowd_sourcing.data.service.alarm

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AlarmService {
    @POST("/api/v1/marts/cancel")
    suspend fun postCancel(
        @Query("workId") workId : Int,
        @Query("memberId") memberId : Int
    ): Response<String>

    @POST("/api/v1/marts/accept")
    suspend fun postAccept(
        @Query("workId") workId : Int,
        @Query("memberId") memberId : Int
    ): Response<String>

    @GET("/api/v1/marts/send-notifications")
    suspend fun getSendNotification(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double
    ): Response<String>
}