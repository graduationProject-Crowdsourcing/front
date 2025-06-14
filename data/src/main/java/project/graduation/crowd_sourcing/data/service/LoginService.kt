package project.graduation.crowd_sourcing.data.service

import project.graduation.crowd_sourcing.data.request.LoginRequest
import project.graduation.crowd_sourcing.data.request.RefreshTokenRequest
import project.graduation.crowd_sourcing.data.request.SignUpRequest
import project.graduation.crowd_sourcing.data.response.login.LoginResponse
import project.graduation.crowd_sourcing.data.response.login.RefreshTokenResponse
import project.graduation.crowd_sourcing.data.response.login.SignUpResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface LoginService {
    @POST("/api/v1/accounts/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("/api/v1/accounts/register")
    suspend fun signUp(@Body request: SignUpRequest): Response<SignUpResponse>

    @POST("/api/v1/accounts/refresh")
    suspend fun refreshToken(
        @Body request: RefreshTokenRequest
    ): retrofit2.Response<RefreshTokenResponse>

    @DELETE("/api/v1/accounts/logout")
    suspend fun logout(
        @Header("Authorization") accessToken: String
    ): Response<Void>

}
