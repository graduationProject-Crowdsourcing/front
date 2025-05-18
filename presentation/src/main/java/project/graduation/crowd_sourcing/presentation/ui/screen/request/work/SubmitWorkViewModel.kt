package project.graduation.crowd_sourcing.presentation.ui.screen.request.work

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import project.graduation.crowd_sourcing.domain.usecase.OcrRequestUseCase
import project.graduation.crowd_sourcing.domain.usecase.UploadImageUseCase
import project.graduation.crowd_sourcing.presentation.utils.FileUtil
import java.io.File
import javax.inject.Inject

@HiltViewModel
class SubmitWorkViewModel @Inject constructor(
    private val uploadImageUseCase: UploadImageUseCase,
    private val ocrRequestUseCase: OcrRequestUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SubmitWorkUiState())
    val uiState: StateFlow<SubmitWorkUiState> = _uiState

    fun loadWorkInfo(workId: String) {
        // TODO: 추후 작업 상세 API 연동 예정
        _uiState.value = _uiState.value.copy(workId = workId)
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

        val fileName = "${username}_${System.currentTimeMillis()}.jpg"
        val directoryPath = "images/$fileName"

        viewModelScope.launch {
            val uploadResult = uploadImageUseCase(username, directoryPath, file)

            uploadResult.onSuccess {
                // OCR 요청 이어서 호출
                val ocrResult = ocrRequestUseCase(directoryPath, commissionId)
                ocrResult.onSuccess { text ->
                    // 성공 시 로그 or 상태 업데이트
                    println("✅ OCR 결과: $text")
                }.onFailure {
                    println("❌ OCR 실패: ${it.message}")
                }
            }.onFailure {
                println("❌ 이미지 업로드 실패: ${it.message}")
            }
        }
    }
}
