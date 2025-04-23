package project.graduation.crowd_sourcing.presentation.ui.screen.request.work

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// 작업 제출 페이지 뷰모델
class SubmitWorkViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SubmitWorkUiState())
    val uiState: StateFlow<SubmitWorkUiState> = _uiState

    fun loadWorkInfo(workId: String) {
        // TODO: 실제 데이터 불러오는 로직 연결
        val work = WorkRepository.getWorkById(workId)
        _uiState.value = SubmitWorkUiState(
            workId = workId,
            title = work?.title.orEmpty(),
            place = work?.place.orEmpty(),
            reward = work?.reward ?: 0,
        )
    }
    fun updatePrice(price: String) {
        _uiState.value = _uiState.value.copy(price = price)
    }

    fun updateExecuteTime(time: String) {
        _uiState.value = _uiState.value.copy(executeTime = time)
    }

    fun updateImage(uri: Uri?) {
        _uiState.value = _uiState.value.copy(imageUri = uri)
    }

    fun verifyLocation() {
        _uiState.value = _uiState.value.copy(locationVerified = true)
    }

    fun isSavable(): Boolean {
        val state = uiState.value
        return state.price.isNotBlank() &&
                state.executeTime.isNotBlank() &&
                state.locationVerified
    }
}
