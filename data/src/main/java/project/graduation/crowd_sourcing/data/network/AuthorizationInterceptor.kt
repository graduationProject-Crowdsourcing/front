package project.graduation.crowd_sourcing.data.network

import okhttp3.Interceptor
import okhttp3.Response


class AuthorizationInterceptor: Interceptor {
    // 실제 Bearer 토큰 입력 : 추후 로그인 기능 구현되면 바꿔야함
    private val accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIzIiwidG9rZW5UeXBlIjoiQUNDRVNTIiwibWVtYmVyUm9sZSI6IlJPTEVfVVNFUiIsImlhdCI6MTc0NzQ5MTgwNSwiZXhwIjoxNzQ3NDkzNjA1fQ.VYilzxZvg2bp_xtMs_0RxRB8VonbSPhWqcU-6ISAtX4"
    
    override fun intercept(
        chain: Interceptor.Chain
    ): Response {
        val originalRequest = chain.request()
        println("DEBUG_AUTH: 원본 요청 URL: ${originalRequest.url}")
        
        // 기존 요청에 Authorization 헤더 추가
        val newRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer ${accessToken}")  // header로 변경 (기존 헤더 덮어쓰기)
            .header("accept", "*/*")             // Accept 헤더 추가
            .build()
        
        // 모든 헤더 확인
        println("DEBUG_AUTH: 요청에 토큰 추가됨")
        println("DEBUG_AUTH: 모든 헤더:")
        newRequest.headers.forEach { header ->
            println("DEBUG_AUTH: ${header.first}: ${header.second}")
        }
        
        // 요청 실행 후 응답 확인
        val response = chain.proceed(newRequest)
        println("DEBUG_AUTH: 응답 코드: ${response.code}")
        println("DEBUG_AUTH: 응답 메시지: ${response.message}")
        
        return response
    }
}