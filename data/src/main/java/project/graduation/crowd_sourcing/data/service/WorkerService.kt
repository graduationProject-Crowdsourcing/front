package project.graduation.crowd_sourcing.data.service

import project.graduation.crowd_sourcing.data.request.worker.PostAssignmentRequest
import project.graduation.crowd_sourcing.data.request.worker.PostWorkRequest
import project.graduation.crowd_sourcing.data.response.worker.WorkCountResponse
import project.graduation.crowd_sourcing.data.response.worker.WorkHistoryResponse
import project.graduation.crowd_sourcing.data.response.worker.WorkHourResponse
import project.graduation.crowd_sourcing.data.response.worker.WorkMostResponse
import project.graduation.crowd_sourcing.data.response.worker.WorkOngoingResponse
import project.graduation.crowd_sourcing.data.response.worker.WorkPointResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


interface WorkerService {
    @Headers("Content-Type: application/json")
    @POST("/api/v1/worker/postwork")
    suspend fun postWorker(
        @Body request: PostWorkRequest
    ): Response<Int>

    @POST("/api/v1/worker/assignment/{assignmentId}/submit")
    suspend fun postAssignment(
        @Body request: PostAssignmentRequest,
        @Path("assignmentId") assignmentId: Int,
        @Query("username") username: String
    ): Response<Unit>


    @GET("/api/v1/worker/workstats")
    suspend fun getWorkerCounts(
        @Query("username") username: String
    ): WorkCountResponse

    @GET("/api/v1/worker/workpoint")
    suspend fun getWorkerPoint(
        @Query("username") username: String
    ): WorkPointResponse

    @GET("/api/v1/worker/workongoing")
    suspend fun getWorking(
        @Query("username") username: String
    ): List<WorkOngoingResponse>

    @GET("/api/v1/worker/workhour")
    suspend fun getWorkerHour(
        @Query("username") username: String
    ): WorkHourResponse

    @GET("/api/v1/worker/workhistory")
    suspend fun getWorkHistory(
        @Query("username") username: String,
        @Query("status") workingStatus: String // Available values : COMPLETED, CANCEL
    ): List<WorkHistoryResponse>

    @GET("/api/v1/worker/workDetail")
    suspend fun getWorkMost(
        @Query("username") username: String
    ): WorkMostResponse
}

