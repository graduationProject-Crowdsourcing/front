package project.graduation.crowd_sourcing.presentation.ui.screen.request.work

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import project.graduation.crowd_sourcing.domain.usecase.UploadImageUseCase
import project.graduation.crowd_sourcing.presentation.utils.FileUtil
import java.io.File
import javax.inject.Inject

@HiltViewModel
class SubmitWorkViewModel @Inject constructor(
    private val uploadImageUseCase: UploadImageUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SubmitWorkUiState())
    val uiState: StateFlow<SubmitWorkUiState> = _uiState

    fun loadWorkInfo(workId: String) {
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

    // 이미지 업로드 로직
    fun uploadImage(context: Context, username: String, commissionId: String) {
        val uri = _uiState.value.imageUri ?: return
        val file = FileUtil.from(context, uri)
        val directoryPath = "$username/${commissionId}_${System.currentTimeMillis()}.jpg"

        viewModelScope.launch {
            val result = uploadImageUseCase(username, directoryPath, file)
            result.onSuccess {
                // 성공 처리: 예를 들어 OCR API 호출 or 상태 저장
            }.onFailure {
                // 실패 처리: 로그, 토스트 등
            }
        }
    }
}
