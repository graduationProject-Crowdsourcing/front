package project.graduation.crowd_sourcing.presentation.ui.screen.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState : StateFlow<HomeUiState> = _uiState.asStateFlow()

    // 쿼리리 업데이트
    fun updateSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    // 현재 위치 업데이트
    fun updateCurrentLocation(latitude: Double, longitude: Double) {
        _uiState.value = _uiState.value.copy(
            currentLocation = Location(latitude, longitude)
        )
    }

    // 의뢰 목록 업데이트
    fun updateRequests(requests: List<Request>) {
        _uiState.value = _uiState.value.copy(requests = requests)
    }


    // 테스트용 더미데이터
    init {
        loadDummyData()
    }

    private fun loadDummyData() {
        val dummyRequests = listOf(
            Request(
                id = "1",
                title = "딸기 한 팩 가격",
                location = Location(37.5665, 126.9780),
                place = "수색 이마트",
                reward = 15
            ),
            Request(
                id = "2",
                title = "신촌역 주변 카페 가격",
                location = Location(37.5558, 126.9368),
                place = "신촌역",
                reward = 20
            ),
            Request(
                id = "3",
                title = "홍대입구역 음식점 메뉴",
                location = Location(37.5571, 126.9254),
                place = "홍대입구역",
                reward = 25
            )
        )
        updateRequests(dummyRequests)
    }
} 