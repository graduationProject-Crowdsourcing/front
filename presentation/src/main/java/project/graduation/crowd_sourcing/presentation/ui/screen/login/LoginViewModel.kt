package project.graduation.crowd_sourcing.presentation.ui.screen.login

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import project.graduation.crowd_sourcing.domain.local.TokenManager
import project.graduation.crowd_sourcing.domain.usecase.MemberUseCase
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val memberUseCase: MemberUseCase,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState.init())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    var isSignUpCompleted = false
        private set

    var isLoginSuccess by mutableStateOf(false)
        private set

    fun onEmailChanged(value: String) {
        _uiState.value = _uiState.value.copy(email = value)
        validate()
    }

    fun onPasswordChanged(value: String) {
        _uiState.value = _uiState.value.copy(password = value)
        validate()
    }

    fun onLoginClick() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(errorMessage = null)

            memberUseCase.login(_uiState.value.email, _uiState.value.password)
                .onSuccess {
                    isLoginSuccess = true

                    Log.d("Login", "🔐 저장된 accessToken = ${tokenManager.getAccessToken()}")

                }
                .onFailure {
                    // 로그인 실패 시
                    isLoginSuccess = false
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "로그인에 실패했습니다. 다시 시도해 주세요."
                    )
                }
        }
    }

    fun signUp(username: String, password: String, nickname: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(errorMessage = null)

            memberUseCase.signUp(username, password, nickname)
                .onSuccess {
                    isSignUpCompleted = true
                    _uiState.value = _uiState.value.copy(errorMessage = null)
                }
                .onFailure {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = it.message ?: "회원가입에 실패했습니다."
                    )
                }
        }
    }

    fun onSignUpSuccess() {
        isSignUpCompleted = true
    }

    private fun validate() {
        val valid = _uiState.value.email.isNotBlank() && _uiState.value.password.isNotBlank()
        _uiState.value = _uiState.value.copy(
            isLoginEnabled = valid,
            errorMessage = null
        )
    }
}