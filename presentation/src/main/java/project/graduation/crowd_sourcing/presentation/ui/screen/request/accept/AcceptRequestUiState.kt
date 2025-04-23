package project.graduation.crowd_sourcing.presentation.ui.screen.request.accept

// 의뢰 수락 UiState
data class AcceptRequestUiState(
    val id: String = "",
    val place: String = "",
    val title: String = "",
    val reward: Int = 0,
    val participant: String = "",
    val deadline: String = ""
)