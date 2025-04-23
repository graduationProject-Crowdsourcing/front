package project.graduation.crowd_sourcing.presentation.ui.screen.request.work

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

// 작업 제출 버튼 클릭 시 등장 - 작업 리스트 페이지 뷰모델
data class Work(
    val id: String,
    val title: String,
    val place: String,
    val reward: Int
)

class WorkListViewModel : ViewModel() {
    var workList by mutableStateOf(WorkRepository.workList)
}