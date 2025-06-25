package project.graduation.crowd_sourcing.data.network

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import project.graduation.crowd_sourcing.domain.local.TokenManager
import javax.inject.Inject

class AuthorizationInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val accessToken = tokenManager.getAccessToken()

        val path = originalRequest.url.encodedPath
        Log.d("InterceptorDebug", "요청 path = $path")
        Log.d("InterceptorDebug", "accessToken = $accessToken")

        if (
            accessToken.isNullOrBlank() ||
            path.startsWith("/api/v1/accounts/register") ||
            path.startsWith("/api/v1/accounts/login") ||
            path.startsWith("/api/v1/accounts/refresh")
        ) {
            Log.d("InterceptorDebug", "인증 제외 경로 또는 토큰 없음 → 원본 요청 진행")
            return chain.proceed(originalRequest)
        }


        val newRequest = originalRequest.newBuilder()
            .addHeader("Authorization", "Bearer $accessToken")
            .build()

        Log.d("InterceptorDebug", "Authorization 헤더 추가: Bearer $accessToken")
        return chain.proceed(newRequest)
    }
}


