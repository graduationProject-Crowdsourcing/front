package project.graduation.crowd_sourcing.presentation.ui.screen.home

// TODO: Domain Layer 구현 필요
// - Location 도메인 모델 정의
// - Request 도메인 모델 정의
// - 상태 관련 도메인 로직 분리

// 변경 내역:
// 1. 상태 클래스 분리
//    - HomeViewModel에서 상태 클래스를 별도 파일로 분리
//    - 상태 관련 로직을 한 곳에서 관리

sealed class HomeUiState {
    data class Success(
        val searchQuery: String = "",
        val currentLocation: Location? = null,
        val requests: List<Request> = emptyList(),
        val searchRadius: Float = 0.5f,
        val isRadiusDialogVisible: Boolean = false
    ) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
    object Loading : HomeUiState()
}

data class Location(
    val latitude: Double,
    val longitude: Double
)

data class Request(
    val id: String,
    val title: String,
    val location: Location,
    val place: String,
    val reward: Int
) 