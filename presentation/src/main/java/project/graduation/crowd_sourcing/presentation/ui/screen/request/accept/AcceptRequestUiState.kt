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
    val latitude: Double = 37.5818, // 마트 위치 위도 (마트 검색을 통해 설정, 기본값: 청량리 롯데마트)
    val longitude: Double = 127.0368, // 마트 위치 경도 (마트 검색을 통해 설정, 기본값: 청량리 롯데마트)
    val isAcceptLoading: Boolean = false,
    val isAcceptSuccess: Boolean = false,
    val acceptErrorMessage: String? = null
)