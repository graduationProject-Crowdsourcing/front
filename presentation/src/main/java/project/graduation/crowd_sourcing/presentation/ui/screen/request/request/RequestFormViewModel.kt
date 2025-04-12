package project.graduation.crowd_sourcing.presentation.viewmodel.request

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import project.graduation.crowd_sourcing.presentation.ui.screen.request.request.RequestFormUiState

class RequestFormViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RequestFormUiState())
    val uiState: StateFlow<RequestFormUiState> = _uiState

    fun onMartChange(value: String) {
        _uiState.update { it.copy(martName = value) }
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

    // 필요 시 초기화 함수도 추가 가능
    fun resetForm() {
        _uiState.value = RequestFormUiState()
    }
}
