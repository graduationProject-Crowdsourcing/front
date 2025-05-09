package project.graduation.crowd_sourcing.presentation.ui.screen.home

import project.graduation.crowd_sourcing.domain.model.entity.Mart

// TODO: Domain Layer 구현 필요
// - Location 도메인 모델 정의
// - Request 도메인 모델 정의
// - 상태 관련 도메인 로직 분리

// 변경 내역:
// 1. 상태 클래스 분리
//    - HomeViewModel에서 상태 클래스를 별도 파일로 분리
//    - 상태 관련 로직을 한 곳에서 관리

/**
 * 홈 화면의 UI 상태를 관리하는 sealed class
 * 앱의 상태에 따라 Loading, Success, Error 상태로 구분됨
 */
sealed class HomeUiState {
    /**
     * 데이터 로딩이 성공적으로 완료된 상태
     * 
     * @param searchQuery 검색어
     * @param currentLocation 사용자의 현재 위치 (null일 경우 위치 정보 없음)
     * @param requests 주변 의뢰 목록
     * @param searchRadius 검색 반경 (km 단위)
     * @param isRadiusDialogVisible 반경 설정 다이얼로그 표시 여부
     * @param nearbyMarts 주변 마트 목록
     */
    data class Success(
        val searchQuery: String = "",
        val currentLocation: Location? = null,
        val requests: List<Request> = emptyList(),
        val searchRadius: Float = 0.5f,
        val isRadiusDialogVisible: Boolean = false,
        val nearbyMarts: List<Mart> = emptyList()
    ) : HomeUiState()
    
    /**
     * 오류 발생 상태
     * 
     * @param message 오류 메시지
     */
    data class Error(val message: String) : HomeUiState()
    
    /**
     * 초기 로딩 상태
     */
    object Loading : HomeUiState()
}

/**
 * 위치 정보 데이터 클래스
 * 
 * @param latitude 위도
 * @param longitude 경도
 */
data class Location(
    val latitude: Double,
    val longitude: Double
)

/**
 * 의뢰 정보 데이터 클래스
 * 
 * @param id 의뢰 고유 ID
 * @param title 의뢰 제목
 * @param location 의뢰 위치
 * @param place 의뢰 장소명
 * @param reward 의뢰 보상금액 (원)
 */
data class Request(
    val id: String,
    val title: String,
    val location: Location,
    val place: String,
    val reward: Int
) 