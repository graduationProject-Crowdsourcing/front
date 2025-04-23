package project.graduation.crowd_sourcing.presentation.ui.screen.request.request

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import project.graduation.crowd_sourcing.presentation.ui.screen.request.request.MartInfo
import project.graduation.crowd_sourcing.presentation.ui.screen.request.request.RequestFormUiState

open class RequestFormViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RequestFormUiState())
    val uiState: StateFlow<RequestFormUiState> = _uiState

    /** 마트 이름만 업데이트 (UI 전용) */
    fun onMartChange(value: String) {
        _uiState.update { it.copy(martName = value) }
    }

    /** 마트 전체 정보(MartInfo)를 전달받아 상태 갱신 */
    fun setSelectedMart(mart: MartInfo) {
        _uiState.update {
            it.copy(
                martName = mart.name,
                martLat = mart.latitude,
                martLng = mart.longitude
            )
        }
    }

    fun onMaxPeopleChange(value: String) {
        _uiState.update { it.copy(maxPeople = value) }
    }

    fun onPointPerPersonChange(value: String) {
        _uiState.update { it.copy(pointPerPerson = value) }
    }

    fun onItemChange(value: String) {
        _uiState.update { it.copy(item = value) }
    }

    fun onDateTimeChange(value: String) {
        _uiState.update { it.copy(dateTime = value) }
    }
}
