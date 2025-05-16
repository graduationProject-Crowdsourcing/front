package project.graduation.crowd_sourcing.data.network

import okhttp3.Interceptor
import okhttp3.Response
import project.graduation.crowd_sourcing.data.local.TokenManager
import javax.inject.Inject

class AuthorizationInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = tokenManager.getAccessToken()

        val newRequest = if (token != null) {
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            chain.request()
        }

        return chain.proceed(newRequest)
    }
}
