package project.graduation.crowd_sourcing.presentation.ui.screen.logout

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import project.graduation.crowd_sourcing.data.local.TokenManager
import project.graduation.crowd_sourcing.domain.usecase.MemberUseCase
import javax.inject.Inject

@HiltViewModel
class LogoutConfirmViewModel @Inject constructor(
    private val memberUseCase: MemberUseCase,
    private val tokenManager: TokenManager
) : ViewModel() {

    var logoutSuccess by mutableStateOf(false)
        private set

    fun logout() {
        viewModelScope.launch {
            val accessToken = tokenManager.getAccessToken()
            Log.d("Logout", "🔑 accessToken = $accessToken")

            memberUseCase.logout(accessToken ?: "")
                .onSuccess {
                    tokenManager.clear()
                    logoutSuccess = true
                }
                .onFailure {
                    Log.e("Logout", "❌ 로그아웃 실패: ${it.message}")
                }
        }
    }
}
