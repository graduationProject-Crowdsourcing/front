package project.graduation.crowd_sourcing.presentation.ui.screen.request.request

import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import project.graduation.crowd_sourcing.domain.usecase.MartSearchUseCase
import project.graduation.crowd_sourcing.domain.usecase.RequesterUseCase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject
import android.util.Log
import androidx.annotation.RequiresApi
import project.graduation.crowd_sourcing.domain.local.TokenManager
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@HiltViewModel
class RequestFormViewModel @Inject constructor(
    private val requesterUseCase: RequesterUseCase,
    private val tokenManager: TokenManager
) : ViewModel() {

    // 서울시 지역구 목록
    private val seoulDistricts = listOf(
        "강남구", "강동구", "강북구", "강서구", "관악구", "광진구", "구로구", "금천구",
        "노원구", "도봉구", "동대문구", "동작구", "마포구", "서대문구", "서초구",
        "성동구", "성북구", "송파구", "양천구", "영등포구", "용산구", "은평구",
        "종로구", "중구", "중랑구"
    )

    private val _uiState = MutableStateFlow(RequestFormUiState())
    val uiState: StateFlow<RequestFormUiState> = _uiState

    // 의뢰 등록 상태
    private val _requestState = MutableStateFlow<RequestState>(RequestState.Initial)
    val requestState: StateFlow<RequestState> = _requestState

    // 지역 선택 - 자동완성 추천 리스트 상태
    private val _districtSuggestions = MutableStateFlow<List<String>>(emptyList())
    val districtSuggestions: StateFlow<List<String>> = _districtSuggestions.asStateFlow()

    // 지역구 입력 시 호출
    fun onSigunguChange(input: String) {
        _uiState.update { it.copy(sigungu = input) }

        // 필터링 로직: 입력한 글자가 포함된 지역구만 추림
        if (input.isNotBlank()) {
            _districtSuggestions.value = seoulDistricts.filter { it.contains(input) }
        } else {
            _districtSuggestions.value = emptyList()
        }
    }

    // 추천 리스트에서 항목 클릭 시 호출
    fun onDistrictSelected(name: String) {
        _uiState.update { it.copy(sigungu = name) }
        _districtSuggestions.value = emptyList()
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

    fun onExpirationDateChange(value: String) {
        _uiState.update { it.copy(expirationDate = value) }
    }
    
    // 의뢰 생성
    @RequiresApi(Build.VERSION_CODES.O)
    fun submitRequest() {
        val state = _uiState.value
        
        // 입력값 유효성 검사
        if (!validateInputs(state)) {
            _requestState.value = RequestState.Error("모든 필드를 입력해주세요.")
            return
        }
        
        viewModelScope.launch {
            _requestState.value = RequestState.Loading
            
            try {
                // 문자열 타입 숫자 변환
                val maxPeople = state.maxPeople.toIntOrNull() ?: 0
                val pointPerPerson = state.pointPerPerson.toIntOrNull() ?: 0
                
                // 날짜 파싱 - 사용자가 입력한 형식 (yyyy-MM-dd HH:mm)
                val userDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                userDateFormat.timeZone = TimeZone.getDefault() // 로컬 시간대 설정

                // 디버그 로그 추가
                Log.d("RequestFormViewModel", "현재 시스템 시간: ${Date()}")
                Log.d("RequestFormViewModel", "파싱 시도할 사용자 입력 날짜 문자열: ${state.expirationDate}")
                
                try {
                    // 사용자 입력 날짜 문자열을 Date 객체로 파싱
                    val userDate = userDateFormat.parse(state.expirationDate)
                    if (userDate != null) {
                        Log.d("RequestFormViewModel", "사용자 입력 날짜: ${state.expirationDate}, 파싱된 날짜: ${userDate}")
                        Log.d("RequestFormViewModel", "현재 시간과의 차이(밀리초): ${userDate.time - Date().time}")
                        
                        // 날짜가 이미 지난 경우
                        if (userDate.before(Date())) {
                            _requestState.value = RequestState.Error("마감 시간은 현재 시간 이후로 설정해야 합니다.")
                            return@launch
                        }
                        
                        // API 호출 전에 날짜를 ISO 8601 형식으로 포맷팅하여 로그로 확인
                        val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                        // UTC 시간대 사용
                        isoFormat.timeZone = TimeZone.getTimeZone("UTC")
                        val isoDateString = isoFormat.format(userDate)
                        Log.d("RequestFormViewModel", "ISO 8601 형식으로 변환한 날짜: $isoDateString")

                        val userId = tokenManager.getUserId()
                        if (userId == -1) {
                            _requestState.value = RequestState.Error("로그인이 필요합니다. 다시 로그인해 주세요.")
                            return@launch
                        }

                        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")

                        val workDate = LocalDateTime.now().format(formatter)
                        val expirationDate = LocalDateTime.parse(isoDateString, formatter).format(formatter)

                        // API 호출
                        val result = requesterUseCase.postWork(
                            work = "가격조사",
                            workCount = maxPeople,
                            workpoint = pointPerPerson,
                            martName = "", // 백엔드에서 처리 예정, 현재는 placeholder로 빈 문자열 사용
                            sigungu = state.sigungu,
                            item = state.item,
                            workDate = workDate,
                            memberId = userId,
                            category = "라면",  // TODO: 추후에 하드코딩 없애기
                            workhour = 2,       // TODO: 추후에 하드코딩 없애기
                            expirationDate = expirationDate
                        )
                        Log.d("RequestFormViewModel", "postWork 결과: $result")
                        _requestState.value = RequestState.Success(result)

                    } else {
                        _requestState.value = RequestState.Error("날짜 형식이 올바르지 않습니다.")
                    }
                } catch (e: Exception) {
                    Log.e("RequestFormViewModel", "날짜 파싱 오류", e)
                    _requestState.value = RequestState.Error("날짜 형식이 올바르지 않습니다: ${e.message}")
                }
            } catch (e: Exception) {
                // 상세한 오류 메시지 제공
                val errorMessage = when {
                    e.message?.contains("403") == true -> "권한이 없습니다. 로그인 세션이 만료되었을 수 있습니다."
                    e.message?.contains("401") == true -> "인증 오류가 발생했습니다. 다시 로그인해 주세요."
                    e.message?.contains("400") == true -> "입력값이 올바르지 않습니다."
                    e.message?.contains("500") == true -> "서버 오류가 발생했습니다. 잠시 후 다시 시도해 주세요."
                    e.message?.contains("timeout") == true || e.message?.contains("timed out") == true -> 
                        "서버 연결 시간이 초과되었습니다. 네트워크 상태를 확인해 주세요."
                    else -> "의뢰 등록 중 오류가 발생했습니다: ${e.message ?: "알 수 없는 오류"}"
                }
                
                // 로그에 상세 오류 정보 기록
                Log.e("RequestFormViewModel", "의뢰 등록 오류", e)
                
                _requestState.value = RequestState.Error(errorMessage)
            }
        }
    }
    
    // 입력값 유효 검사
    private fun validateInputs(state: RequestFormUiState): Boolean {
        return state.sigungu.isNotBlank() &&
                state.maxPeople.isNotBlank() &&
                state.pointPerPerson.isNotBlank() &&
                state.item.isNotBlank() &&
                state.expirationDate.isNotBlank()
    }
    
    // 요청 상태 초기화
    fun resetRequestState() {
        _requestState.value = RequestState.Initial
    }
}

// 의뢰 등록 요청 상태
sealed class RequestState {
    object Initial : RequestState()
    object Loading : RequestState()
    data class Success(val requestId: Result<Int>) : RequestState()
    data class Error(val message: String) : RequestState()
}
