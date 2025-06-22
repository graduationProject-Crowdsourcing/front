package project.graduation.crowd_sourcing.presentation.ui.screen.request.accept

// 의뢰 수락 UiState
data class AcceptRequestUiState(
    val commissionId: Int = 0,
    val commission: String = "",
    val region: String = "",
    val martName: String = "",
    val category: String = "",
    val item: String = "",
    val commissionPoint: Int = 0,
    val commissionCount: Int = 0,
    val createdAt: String = "",
    val expirationDate: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val latitude: Double = 37.5818, // 청량리 롯데마트 위도 (기본값)
    val longitude: Double = 127.0368 // 청량리 롯데마트 경도 (기본값)
)