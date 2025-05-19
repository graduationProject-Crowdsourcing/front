package project.graduation.crowd_sourcing.data.network

import okhttp3.Interceptor
import okhttp3.Response


class AuthorizationInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI0IiwidG9rZW5UeXBlIjoiQUNDRVNTIiwibWVtYmVyUm9sZSI6IlJPTEVfVVNFUiIsImlhdCI6MTc0NzI5MDk5OCwiZXhwIjoxNzQ3MjkyNzk4fQ.AGV9hBT4dr6TNMbloyq1Mn0vvBhc7uCxgoyOVrRyIU0 "

        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer ${accessToken}")
            .build()
        return chain.proceed(request)
    }
}