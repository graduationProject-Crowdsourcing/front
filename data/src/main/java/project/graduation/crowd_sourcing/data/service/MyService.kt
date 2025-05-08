package project.graduation.crowd_sourcing.data.service

import project.graduation.crowd_sourcing.data.response.my.RecentCommissionResponse
import project.graduation.crowd_sourcing.data.response.my.RecentWorkResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface MyService {
    @GET("/api/v1/mypage/recent-work")
    fun getRecentWork(
        @Query("userId") userId: Int
    ): RecentWorkResponse

    @GET("/api/v1/mypage/recent-commission")
    fun getRecentCommission(
        @Query("userId") userId: Int
    ): RecentCommissionResponse
}