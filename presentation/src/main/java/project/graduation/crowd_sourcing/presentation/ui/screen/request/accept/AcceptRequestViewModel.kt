package project.graduation.crowd_sourcing.presentation.ui.screen.request.accept

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

// 더미 데이터
open class AcceptRequestViewModel : ViewModel() {
    var uiState by mutableStateOf(
        AcceptRequestUiState(
            id = "1",
            place = "청량리 롯데마트",
            title = "딸기 한 박스",
            reward = 100,
            participant = "2/5",
            deadline = "2025/04/18 (금) 00:00",
            latitude = 37.5818, // 청량리 롯데마트 위도
            longitude = 127.0368 // 청량리 롯데마트 경도
        )
    )

    // 의뢰 정보를 외부에서 설정할 수 있는 함수 (추후 백엔드 연동용)
    fun setRequestInfo(
        id: String,
        place: String,
        title: String,
        reward: Int,
        participant: String,
        deadline: String,
        latitude: Double,
        longitude: Double
    ) {
        uiState = AcceptRequestUiState(
            id = id,
            place = place,
            title = title,
            reward = reward,
            participant = participant,
            deadline = deadline,
            latitude = latitude,
            longitude = longitude
        )
    }
}