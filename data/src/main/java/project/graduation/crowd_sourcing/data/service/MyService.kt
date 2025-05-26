package project.graduation.crowd_sourcing.data.service

import project.graduation.crowd_sourcing.data.request.MyNicknameRequest
import project.graduation.crowd_sourcing.data.response.my.RecentCommissionResponse
import project.graduation.crowd_sourcing.data.response.my.RecentWorkResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query


interface MyService {
    @GET("/api/v1/mypage/recent-work")
    suspend fun getRecentWork(
        @Query("userId") userId: Int
    ): RecentWorkResponse

    @GET("/api/v1/mypage/recent-commission")
    suspend fun getRecentCommission(
        @Query("userId") userId: Int
    ): RecentCommissionResponse

    @PUT("/api/v1/mypage/{memberId}/nickname")
    suspend fun putNickname(
        @Path("memberId") memberId: Int,
        @Body request: MyNicknameRequest
    ): Unit
}