package project.graduation.crowd_sourcing.data.network

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
        if (accessToken.isNullOrBlank() || path.contains("/login")) {
            return chain.proceed(originalRequest)
        }

        val newRequest = originalRequest.newBuilder()
            .addHeader("Authorization", "Bearer $accessToken")
            .build()

        return chain.proceed(newRequest)
    }
}


