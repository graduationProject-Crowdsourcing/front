package project.graduation.crowd_sourcing.data.network

import okhttp3.Interceptor
import okhttp3.Response


class AuthorizationInterceptor: Interceptor {
    override fun intercept(
        chain: Interceptor.Chain
    ): Response {

        val newRequest = chain.request().newBuilder().build()

        return chain.proceed(newRequest)
    }
}