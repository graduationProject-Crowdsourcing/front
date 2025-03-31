package project.graduation.crowd_sourcing.presentation.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// TODO: Domain Layer 구현 필요
// 1. UseCase 주입 및 사용
//    - ViewModel에 UseCase 주입
//    - 더미 데이터 대신 실제 UseCase 사용
// 2. Repository 구현
//    - GoogleMapsRepository: Google Maps API 연동
//    - PlacesRepository: 장소 검색 API 연동
// 3. 에러 처리
//    - UseCase에서 발생하는 예외 처리
//    - 사용자에게 적절한 에러 메시지 표시

// 변경 내역:
// 1. 상태 관리 개선
//    - sealed class를 사용한 상태 관리 도입 (Loading, Success, Error)
//    - 상태 업데이트 시 when 표현식을 사용하여 타입 안전성 확보
// 2. 에러 처리 추가
//    - try-catch 블록을 사용한 예외 처리
//    - 에러 상태에 대한 UI 처리
// 3. 초기화 로직 개선
//    - loadInitialData() 함수를 통한 초기화
//    - viewModelScope를 사용한 코루틴 스코프 관리
// 4. 더미 데이터 관리
//    - companion object를 사용한 더미 데이터 관리
//    - 일관된 네이밍 컨벤션 적용

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    companion object {
        private val DUMMY_REQUESTS = listOf(
            Request(
                id = "1",
                title = "수색 아파트 - 벌기 편백",
                location = Location(37.5820, 126.8895),
                place = "수색 아파트",
                reward = 15000
            ),
            Request(
                id = "2",
                title = "상암 물품 - 벌기 가격",
                location = Location(37.5784, 126.8967),
                place = "상암동",
                reward = 10000
            )
        )
    }

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            try {
                _uiState.update { 
                    HomeUiState.Success(
                        requests = DUMMY_REQUESTS
                    )
                }
            } catch (e: Exception) {
                _uiState.update { 
                    HomeUiState.Error("데이터를 불러오는데 실패했습니다: ${e.message}")
                }
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _uiState.update { currentState ->
            when (currentState) {
                is HomeUiState.Success -> currentState.copy(searchQuery = query)
                else -> currentState
            }
        }
    }

    fun updateCurrentLocation(latitude: Double, longitude: Double) {
        _uiState.update { currentState ->
            when (currentState) {
                is HomeUiState.Success -> currentState.copy(
                    currentLocation = Location(latitude, longitude)
                )
                else -> currentState
            }
        }
    }

    fun updateRequests(requests: List<Request>) {
        _uiState.update { currentState ->
            when (currentState) {
                is HomeUiState.Success -> currentState.copy(requests = requests)
                else -> currentState
            }
        }
    }

    fun showRadiusDialog() {
        _uiState.update { currentState ->
            when (currentState) {
                is HomeUiState.Success -> currentState.copy(isRadiusDialogVisible = true)
                else -> currentState
            }
        }
    }

    fun hideRadiusDialog() {
        _uiState.update { currentState ->
            when (currentState) {
                is HomeUiState.Success -> currentState.copy(isRadiusDialogVisible = false)
                else -> currentState
            }
        }
    }

    fun updateSearchRadius(radius: Float) {
        _uiState.update { currentState ->
            when (currentState) {
                is HomeUiState.Success -> currentState.copy(searchRadius = radius)
                else -> currentState
            }
        }
    }
} 