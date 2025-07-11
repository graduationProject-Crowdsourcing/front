package project.graduation.crowd_sourcing.presentation.ui.screen.request.work

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import project.graduation.crowd_sourcing.domain.usecase.MartSearchUseCase
import project.graduation.crowd_sourcing.domain.usecase.OcrRequestUseCase
import project.graduation.crowd_sourcing.domain.usecase.WorkerUseCase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class SubmitWorkViewModel @Inject constructor(
    private val ocrRequestUseCase: OcrRequestUseCase,
    private val workerUseCase: WorkerUseCase,
    private val martSearchUseCase: MartSearchUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SubmitWorkUiState())
    val uiState: StateFlow<SubmitWorkUiState> = _uiState

    fun loadWorkInfo(workId: Int, martName: String, category :String) {

        _uiState.value = _uiState.value.copy(id = workId, category = category, place = martName)
    }

    fun updatePrice(price: String) {
        _uiState.value = _uiState.value.copy(price = price)
    }

    fun updateItem(item: String) {
        _uiState.value = _uiState.value.copy(item = item)
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
                state.locationVerified &&
                state.item.isNotBlank() &&
                state.id != null
    }

    // 이미지 업로드 로직
    fun uploadImage(context: Context, uri: Uri) {
        val commissionId = uiState.value.id.toString()

        viewModelScope.launch {
            ocrRequestUseCase(uri, commissionId)
                .onSuccess { list ->
                    _uiState.value = uiState.value.copy(imageUri = uri)
                    Log.d("ocr", "✅ OCR 결과: $list")
                }.onFailure {
                    it.printStackTrace()
                }
        }
    }

    fun submitWork(complete: () -> Unit) = viewModelScope.launch {
        uiState.value.let {
            workerUseCase.postAssignment(
                item = it.item,
                itemPrice = it.price.toInt(),
                workDate = parseToLocalDateTime(it.executeTime),
                martName = it.place,
                assignmentId = it.id!!.toInt()
            ).onSuccess {
                complete()
            }
        }
    }

    fun locationVerified(lat: Double, lng: Double) {
        viewModelScope.launch {
            try {
                val radius = 0.1 // 100미터 이내

                val marts = martSearchUseCase.searchMartByLocation(lat, lng, radius)
                if(marts.map { it.martName }.contains(uiState.value.place)){
                    _uiState.update{prev->
                        prev.copy(locationVerified = true)
                    }
                }
            } catch (e: Exception) {

            }
        }
    }


    private fun parseToLocalDateTime(dateTimeStr: String): LocalDateTime {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        return LocalDateTime.parse(dateTimeStr, formatter)
    }
}
