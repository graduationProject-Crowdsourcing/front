package project.graduation.crowd_sourcing.data.network

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import project.graduation.crowd_sourcing.data.local.TokenManager
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class AuthorizationInterceptor @Inject constructor(
    private val tokenManager: TokenManager
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

        if (response.code == 401) {
            response.close()

            val refreshToken = tokenManager.getRefreshToken()

            if (refreshToken != null) {
                val refreshResult: Result<Pair<String, String>> = runBlocking {
                    try {
                        // ✅ 여기서 Retrofit을 직접 생성해서 사용!
                        val retrofit = Retrofit.Builder()
                            .baseUrl("http://52.78.15.153:8112/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build()


                        val loginService = retrofit.create(project.graduation.crowd_sourcing.data.service.LoginService::class.java)
                        val res = loginService.refreshToken(project.graduation.crowd_sourcing.data.request.RefreshTokenRequest(refreshToken))

                        if (res.isSuccessful && res.body() != null) {
                            val data = res.body()!!.data
                            Result.success(data.accessToken to data.refreshToken)
                        } else {
                            Result.failure(Exception("토큰 갱신 실패: ${res.code()}"))
                        }
                    } catch (e: Exception) {
                        Result.failure(e)
                    }
                }

                if (refreshResult.isSuccess) {
                    val (newAccessToken, newRefreshToken) = refreshResult.getOrNull()!!

                    tokenManager.save(
                        accessToken = newAccessToken,
                        refreshToken = newRefreshToken,
                        userId = tokenManager.getUserId() ?: 0
                    )

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

