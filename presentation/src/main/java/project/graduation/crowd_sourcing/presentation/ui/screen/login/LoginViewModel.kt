package project.graduation.crowd_sourcing.presentation.ui.screen.login

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import project.graduation.crowd_sourcing.domain.usecase.LoginUseCase
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState.init())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    var isSignUpCompleted = false
        private set

    var isLoginSuccess = false
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
        val success = (_uiState.value.email == "test" && _uiState.value.password == "1234")
        isLoginSuccess = success

        _uiState.value = if (success) {
            _uiState.value.copy(errorMessage = null)
        } else {
            _uiState.value.copy(errorMessage = "이메일 또는 비밀번호가 올바르지 않습니다.")
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