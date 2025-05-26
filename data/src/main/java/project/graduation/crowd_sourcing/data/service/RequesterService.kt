package project.graduation.crowd_sourcing.data.service

import project.graduation.crowd_sourcing.data.response.requester.RequestDetailDto
import project.graduation.crowd_sourcing.data.response.requester.RequestDto
import project.graduation.crowd_sourcing.data.response.requester.RequestHistoryDto
import project.graduation.crowd_sourcing.data.response.requester.RequestPointDto
import project.graduation.crowd_sourcing.data.response.requester.RequestStatsDto
import project.graduation.crowd_sourcing.data.response.requester.RequestStatusDto
import project.graduation.crowd_sourcing.data.response.requester.RequestSuccessDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface RequesterService {
    @POST("/api/v1/requester/postrequest")
    suspend fun postRequest(
        @Body request: RequestDto
    ): Int
    
    @GET("/api/v1/requester/requeststatus/{username}")
    suspend fun getRequestStatus(
        @Path("username") username: String,
        @Query("commissionStatus") status: String
    ): List<RequestStatusDto>
    
    @GET("/api/v1/requester/requeststats")
    suspend fun getRequestStats(
        @Query("username") username: String
    ): RequestStatsDto
    
    @GET("/api/v1/requester/requestpoint")
    suspend fun getRequestPoint(
        @Query("username") username: String
    ): RequestPointDto
    
    @GET("/api/v1/requester/requestSuccess")
    suspend fun getRequestSuccess(
        @Query("username") username: String
    ): RequestSuccessDto
    
    @GET("/api/v1/requester/requestDetail")
    suspend fun getRequestDetail(
        @Query("username") username: String
    ): RequestDetailDto
    
    @GET("/api/v1/requester/ongoing")
    suspend fun getOngoingRequests(
        @Query("username") username: String
    ): List<RequestStatusDto>
    
    @GET("/api/v1/requester/history")
    suspend fun getRequestHistory(
        @Query("username") username: String,
        @Query("status") status: String
    ): List<RequestHistoryDto>
}