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
            place = "상암 홈플러스",
            title = "딸기 한 박스",
            reward = 100,
            participant = "2/5",
            deadline = "2025/04/18 (금) 00:00"
        )
    )
}