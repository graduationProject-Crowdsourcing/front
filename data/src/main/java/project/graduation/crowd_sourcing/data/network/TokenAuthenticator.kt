package project.graduation.crowd_sourcing.data.network

import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import project.graduation.crowd_sourcing.data.request.RefreshTokenRequest
import project.graduation.crowd_sourcing.data.service.LoginService
import project.graduation.crowd_sourcing.domain.local.TokenManager
import javax.inject.Inject
import javax.inject.Named

class TokenAuthenticator @Inject constructor(
    private val tokenManager: TokenManager,
    @Named("no-auth") private val loginService: LoginService
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        val refreshToken = tokenManager.getRefreshToken() ?: return null

        return try {
            val refreshResponse = runBlocking {
                loginService.refreshToken(RefreshTokenRequest(refreshToken))
            }

            if (!refreshResponse.isSuccessful) return null

            val data = refreshResponse.body()?.data ?: return null

            // accessToken, refreshToken null 여부 체크
            val accessToken = data.accessToken
            val newRefreshToken = data.refreshToken
            if (accessToken.isNullOrBlank() || newRefreshToken.isNullOrBlank()) {
                return null
            }

            tokenManager.save(
                accessToken = accessToken,
                refreshToken = newRefreshToken,
                userId = tokenManager.getUserId()
            )

            response.request.newBuilder()
                .header("Authorization", "Bearer $accessToken")
                .build()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}


