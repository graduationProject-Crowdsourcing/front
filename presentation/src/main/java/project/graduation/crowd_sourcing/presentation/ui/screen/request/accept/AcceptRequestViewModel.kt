package project.graduation.crowd_sourcing.presentation.ui.screen.request.accept

import android.os.Build
import android.util.Log
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
import project.graduation.crowd_sourcing.domain.usecase.MartSearchUseCase
import project.graduation.crowd_sourcing.domain.usecase.PostAcceptUseCase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class AcceptRequestViewModel @Inject constructor(
    private val getCommissionDetailUseCase: GetCommissionDetailUseCase,
    private val postAcceptUseCase: PostAcceptUseCase,
    private val martSearchUseCase: MartSearchUseCase,
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
                
                // 기본 의뢰 정보 업데이트
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
                
                // 마트 이름으로 실제 위치 검색
                searchMartLocation(commissionDetail.martName)
                
            } catch (e: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = "의뢰 정보를 불러오는데 실패했습니다: ${e.message}"
                )
            }
        }
    }

    private fun searchMartLocation(martName: String) {
        viewModelScope.launch {
            try {
                Log.d("AcceptRequestViewModel", "마트 위치 검색 시작: $martName")
                
                // 마트 이름으로 검색 (반경 600m로 설정)
                val martResults = martSearchUseCase.searchMartByKeyword(martName, 0.0)
                
                if (martResults.isNotEmpty()) {
                    // 첫 번째 결과 사용
                    val mart = martResults[0]
                    Log.d("AcceptRequestViewModel", "마트 위치 찾음: ${mart.martName} (${mart.latitude}, ${mart.longitude})")
                    
                    uiState = uiState.copy(
                        latitude = mart.latitude,
                        longitude = mart.longitude
                    )
                } else {
                    Log.w("AcceptRequestViewModel", "마트를 찾을 수 없음: $martName, 기본 위치 사용")
                    // 검색 결과가 없으면 기본 위치 유지 (청량리 롯데마트)
                }
            } catch (e: Exception) {
                Log.e("AcceptRequestViewModel", "마트 위치 검색 실패: ${e.message}")
                // 검색 실패 시 기본 위치 유지
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