package project.graduation.crowd_sourcing.data.service

import project.graduation.crowd_sourcing.data.response.userpoint.UserPointHistoryResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface UserPointService {
    @GET("/api/v1/member/history/{memberId}")
    suspend fun getPointHistory(
        @Path("memberId") memberId: Int
    ): List<UserPointHistoryResponse>
}