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
import project.graduation.crowd_sourcing.domain.local.TokenManager
import project.graduation.crowd_sourcing.domain.usecase.GetCommissionDetailUseCase
import project.graduation.crowd_sourcing.domain.usecase.PostAcceptUseCase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class AcceptRequestViewModel @Inject constructor(
    private val getCommissionDetailUseCase: GetCommissionDetailUseCase,
    private val postAcceptUseCase: PostAcceptUseCase,
    private val tokenManager: TokenManager
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

    fun acceptRequest() {
        val memberId = tokenManager.getUserId()
        if (memberId == -1) {
            uiState = uiState.copy(acceptErrorMessage = "사용자 정보를 찾을 수 없습니다.")
            return
        }

        uiState = uiState.copy(
            isAcceptLoading = true,
            isAcceptSuccess = false,
            acceptErrorMessage = null
        )

        viewModelScope.launch {
            postAcceptUseCase(
                workId = uiState.commissionId,
                memberId = memberId
            ).fold(
                onSuccess = { message ->
                    uiState = uiState.copy(
                        isAcceptLoading = false,
                        isAcceptSuccess = true
                    )
                },
                onFailure = { throwable ->
                    uiState = uiState.copy(
                        isAcceptLoading = false,
                        acceptErrorMessage = throwable.message ?: "작업 수락에 실패했습니다."
                    )
                }
            )
        }
    }

    fun clearAcceptError() {
        uiState = uiState.copy(
            acceptErrorMessage = null
        )
    }
}