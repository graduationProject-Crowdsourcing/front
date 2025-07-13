package project.graduation.crowd_sourcing.domain.usecase

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.suspendCancellableCoroutine
import project.graduation.crowd_sourcing.domain.local.TokenManager
import project.graduation.crowd_sourcing.domain.model.entity.login.LoginEntity
import project.graduation.crowd_sourcing.domain.repository.FcmRepository
import project.graduation.crowd_sourcing.domain.repository.LoginRepository
import javax.inject.Inject
import kotlin.coroutines.resumeWithException

class MemberUseCase @Inject constructor(
    private val repository: LoginRepository,
    private val fcmRepository: FcmRepository,
    private val tokenManager: TokenManager
) {
    // 로그인
    suspend fun login(username: String, password: String): Result<LoginEntity> {
        tokenManager.saveUsername(username)
        return repository.login(username, password).onSuccess { loginEntity ->
            try {
                val fcmToken = getFcmToken()
                fcmRepository.postRegister(memberId = loginEntity.id, fcmToken = fcmToken)
            } catch (e: Exception) {
            }
        }
    }

    // 로그아웃
    suspend fun logout(): Result<Unit> {
        return tokenManager.getAccessToken()?.let{
            repository.logout(it).onSuccess {
                tokenManager.clear()
            }
        } ?: Result.failure(IllegalStateException("로그인된 사용자 정보가 없습니다."))
    }

    suspend fun tokenSave(token:String){
        tokenManager.save(accessToken = token, refreshToken = "", userId = 0,)
    }
    // 회원가입
    suspend fun signUp(username: String, password: String, nickname: String): Result<String> {
        return repository.signUp(username, password, nickname)
    }

    // 토큰 재발급
    suspend fun refreshToken(refreshToken: String): Result<Pair<String, String>> {
        return repository.refreshToken(refreshToken)
    }

    private suspend fun getFcmToken(): String = suspendCancellableCoroutine { cont ->
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("FCM", task.result)
                    cont.resume(task.result, null)
                } else {
                    cont.resumeWithException(task.exception ?: Exception("FCM 토큰 가져오기 실패"))
                }
            }
    }

    fun getIsLogined() : Boolean{
        return tokenManager.getAccessToken()?.let{
           true
        } ?: false
    }
}
