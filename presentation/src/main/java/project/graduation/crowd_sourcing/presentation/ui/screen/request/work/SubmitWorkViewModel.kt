package project.graduation.crowd_sourcing.presentation.ui.screen.request.work

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import project.graduation.crowd_sourcing.domain.usecase.OcrRequestUseCase
import project.graduation.crowd_sourcing.domain.usecase.WorkerUseCase
import javax.inject.Inject

@HiltViewModel
class SubmitWorkViewModel @Inject constructor(
    private val ocrRequestUseCase: OcrRequestUseCase,
    private val workerUseCase: WorkerUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SubmitWorkUiState())
    val uiState: StateFlow<SubmitWorkUiState> = _uiState

    fun loadWorkInfo(workId: Int) {
        // TODO: 추후 작업 상세 API 연동 예정
        _uiState.value = _uiState.value.copy(id = workId)
    }

    fun updatePrice(price: String) {
        _uiState.value = _uiState.value.copy(price = price)
    }

    fun updateExecuteTime(time: String) {
        _uiState.value = _uiState.value.copy(executeTime = time)
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

    // 이미지 업로드 로직
    fun uploadImage(context: Context, uri: Uri) {
        val commissionId = uiState.value.id.toString()

        viewModelScope.launch {
            ocrRequestUseCase(uri, commissionId)
                .onSuccess { text ->
                    _uiState.value = uiState.value.copy(imageUri = uri)
                    Log.d("ocr", "✅ OCR 결과: $text")
                }.onFailure {
                    it.printStackTrace()
                }
        }
    }
}
