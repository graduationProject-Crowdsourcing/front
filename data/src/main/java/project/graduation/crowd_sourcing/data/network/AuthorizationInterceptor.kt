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

        if (
            accessToken.isNullOrBlank() ||
            path.startsWith("/api/v1/accounts/register") ||
            path.startsWith("/api/v1/accounts/login") ||
            path.startsWith("/api/v1/accounts/refresh")
        ) {
            return chain.proceed(originalRequest)
        }


        val newRequest = originalRequest.newBuilder()
            .addHeader("Authorization", "Bearer $accessToken")
            .build()

        Log.d("InterceptorDebug", "🚨 Authorization 붙음: ${newRequest.headers}")



        return chain.proceed(newRequest)
    }
}


