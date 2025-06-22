package project.graduation.crowd_sourcing.presentation.ui.screen.request.accept

// 의뢰 수락 UiState
data class AcceptRequestUiState(
    val id: String = "",
    val place: String = "",
    val title: String = "",
    val reward: Int = 0,
    val participant: String = "",
    val deadline: String = "",
    val latitude: Double = 37.5818, // 청량리 롯데마트 위도 (기본값)
    val longitude: Double = 127.0368 // 청량리 롯데마트 경도 (기본값)
)