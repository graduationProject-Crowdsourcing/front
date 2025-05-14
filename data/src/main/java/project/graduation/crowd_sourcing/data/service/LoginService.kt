package project.graduation.crowd_sourcing.data.service

import project.graduation.crowd_sourcing.data.request.LoginRequest
import project.graduation.crowd_sourcing.data.response.login.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {
    @POST("/api/v1/accounts/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
}
