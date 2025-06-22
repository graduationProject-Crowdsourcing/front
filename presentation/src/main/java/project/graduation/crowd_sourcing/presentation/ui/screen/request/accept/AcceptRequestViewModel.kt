package project.graduation.crowd_sourcing.presentation.ui.screen.request.accept

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import project.graduation.crowd_sourcing.domain.usecase.GetCommissionDetailUseCase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class AcceptRequestViewModel @Inject constructor(
    private val getCommissionDetailUseCase: GetCommissionDetailUseCase
) : ViewModel() {
    
    var uiState by mutableStateOf(AcceptRequestUiState())
        private set

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadCommissionDetail(commissionId: Int) {
        uiState = uiState.copy(isLoading = true, errorMessage = null)
        
        viewModelScope.launch {
            try {
                val commissionDetail = getCommissionDetailUseCase(commissionId)
                
                uiState = uiState.copy(
                    isLoading = false,
                    commissionId = commissionDetail.commissionId,
                    commission = commissionDetail.commission,
                    region = commissionDetail.region,
                    martName = commissionDetail.martName,
                    category = commissionDetail.category,
                    item = commissionDetail.item,
                    commissionPoint = commissionDetail.commissionPoint,
                    commissionCount = commissionDetail.commissionCount,
                    createdAt = formatDateTime(commissionDetail.createdAt),
                    expirationDate = formatDateTime(commissionDetail.expirationDate)
                )
            } catch (e: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = "의뢰 정보를 불러오는데 실패했습니다: ${e.message}"
                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun formatDateTime(dateTime: LocalDateTime): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd (E) HH:mm")
        return dateTime.format(formatter)
    }
}