package project.graduation.crowd_sourcing.presentation.ui.screen.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import project.graduation.crowd_sourcing.domain.usecase.LoginUseCase
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    var uiState by mutableStateOf(LoginUiState.init())
        private set

    var isSignUpCompleted by mutableStateOf(false)
        private set

    fun onEmailChanged(value: String) {
        uiState = uiState.copy(email = value)
        validate()
    }

    fun onPasswordChanged(value: String) {
        uiState = uiState.copy(password = value)
        validate()
    }

    fun onLoginClick() {
        // TODO: UseCase 연동 후 처리
        uiState = uiState.copy(errorMessage = "로그인 실패!") // 테스트용
    }

    fun onSignUpSuccess() {
        isSignUpCompleted = true
    }

    private fun validate() {
        val valid = uiState.email.isNotBlank() && uiState.password.isNotBlank()
        uiState = uiState.copy(isLoginEnabled = valid, errorMessage = null)
    }
}
