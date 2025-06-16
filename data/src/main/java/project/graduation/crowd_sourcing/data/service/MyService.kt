package project.graduation.crowd_sourcing.data.service

import okhttp3.MultipartBody
import project.graduation.crowd_sourcing.data.request.MyNicknameRequest
import project.graduation.crowd_sourcing.data.response.my.ProfileImgResponse
import project.graduation.crowd_sourcing.data.response.my.ProfileResponse
import project.graduation.crowd_sourcing.data.response.my.RecentCommissionResponse
import project.graduation.crowd_sourcing.data.response.my.RecentWorkResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
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
    ): Response<Unit>


    @Multipart
    @POST("/api/v1/mypage/myimage")
    suspend fun postProfileImage(
        @Query("username") username: String,
        @Part file: MultipartBody.Part
    ): Response<ProfileImgResponse>

    // 프로필 조회
    @GET("/api/v1/mypage/profile")
    suspend fun getProfile(
        @Query("username") username: String
    ):Response<ProfileResponse>

    @GET("/api/v1/mypage/myimage")
    suspend fun getProfileImg(
        @Query("username") username: String
    ): Response<ProfileImgResponse>
}