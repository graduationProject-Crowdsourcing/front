package project.graduation.crowd_sourcing.presentation.ui.screen.logout

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import project.graduation.crowd_sourcing.data.local.TokenManager
import project.graduation.crowd_sourcing.domain.usecase.LogoutUseCase
import javax.inject.Inject

@HiltViewModel
class LogoutConfirmViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase,
    private val tokenManager: TokenManager
) : ViewModel() {

    var logoutSuccess by mutableStateOf(false)
        private set

    fun logout() {
        viewModelScope.launch {
            logoutUseCase()
                .onSuccess {
                    tokenManager.clear() // 토큰 초기화
                    logoutSuccess = true // 성공 여부 갱신
                }
                .onFailure {
                    // TODO : 실패 처리 시 추가 로직
                }
        }
    }
}
