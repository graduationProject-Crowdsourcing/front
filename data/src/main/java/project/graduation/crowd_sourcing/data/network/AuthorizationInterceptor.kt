package project.graduation.crowd_sourcing.data.network

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import project.graduation.crowd_sourcing.data.local.TokenManager
import project.graduation.crowd_sourcing.domain.usecase.RefreshTokenUseCase
import javax.inject.Inject

class AuthorizationInterceptor @Inject constructor(
    private val tokenManager: TokenManager,
    private val refreshTokenUseCase: RefreshTokenUseCase
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val token = tokenManager.getAccessToken()

        val requestWithAuth = if (token != null) {
            originalRequest.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            originalRequest
        }

        val response = chain.proceed(requestWithAuth)

        // accessToken 만료 시 처리 (401일 경우)
        if (response.code == 401) {
            response.close() // 이전 응답 닫기

            val refreshToken = tokenManager.getRefreshToken()

            if (refreshToken != null) {
                val refreshResult = runBlocking {
                    refreshTokenUseCase(refreshToken)
                }

                if (refreshResult.isSuccess) {
                    val (newAccessToken, newRefreshToken) = refreshResult.getOrNull()!!

                    // 토큰 갱신
                    tokenManager.save(
                        accessToken = newAccessToken,
                        refreshToken = newRefreshToken,
                        userId = tokenManager.getUserId() ?: 0
                    )

                    // 새로운 토큰으로 재요청
                    val newRequest = originalRequest.newBuilder()
                        .removeHeader("Authorization")
                        .addHeader("Authorization", "Bearer $newAccessToken")
                        .build()

                    return chain.proceed(newRequest)
                }
            }
        }

        return response
    }

}
