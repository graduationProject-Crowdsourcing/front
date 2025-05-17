package project.graduation.crowd_sourcing.data.service

import project.graduation.crowd_sourcing.data.request.LoginRequest
import project.graduation.crowd_sourcing.data.request.SignUpRequest
import project.graduation.crowd_sourcing.data.response.login.LoginResponse
import project.graduation.crowd_sourcing.data.response.login.SignUpResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST

interface LoginService {
    @POST("/api/v1/accounts/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("/api/v1/accounts/register")
    suspend fun signUp(@Body request: SignUpRequest): SignUpResponse

    @DELETE("/api/v1/accounts/logout")
    suspend fun logout(): Response<Unit>

}
