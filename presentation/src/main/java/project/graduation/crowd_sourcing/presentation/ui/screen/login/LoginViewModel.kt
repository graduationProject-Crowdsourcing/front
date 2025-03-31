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

    var isLoginSuccess by mutableStateOf(false)
        private set

    fun onLoginClick() {
        val success = (uiState.email == "test" && uiState.password == "1234")
        isLoginSuccess = success

        uiState = if (success) {
            uiState.copy(errorMessage = null)
        } else {
            uiState.copy(errorMessage = "이메일 또는 비밀번호가 올바르지 않습니다.")
        }
    }

    fun onSignUpSuccess() {
        isSignUpCompleted = true
    }

    private fun validate() {
        val valid = uiState.email.isNotBlank() && uiState.password.isNotBlank()
        uiState = uiState.copy(isLoginEnabled = valid, errorMessage = null)
    }
}
