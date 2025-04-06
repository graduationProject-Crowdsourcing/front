package project.graduation.crowd_sourcing.presentation.ui.screen.login

data class LoginUiState(
    val email: String,
    val password: String,
    val isLoginEnabled: Boolean,
    val errorMessage: String? = null
) {
    companion object {
        fun init() = LoginUiState("", "", false)
    }
}
