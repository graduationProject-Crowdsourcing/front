package project.graduation.crowd_sourcing.presentation.ui.screen.request.request

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

@HiltViewModel
class RequestFormViewModel @Inject constructor(
    private val requesterUseCase: RequesterUseCase,
    private val martSearchUseCase: MartSearchUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RequestFormUiState())
    val uiState: StateFlow<RequestFormUiState> = _uiState
    
    // 의뢰 등록 상태
    private val _requestState = MutableStateFlow<RequestState>(RequestState.Initial)
    val requestState: StateFlow<RequestState> = _requestState
    
    // 마트 검색 결과
    private val _searchResults = MutableStateFlow<List<MartInfo>>(emptyList())
    val searchResults: StateFlow<List<MartInfo>> = _searchResults.asStateFlow()
    
    // 마트 검색 중 상태
    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()
    
    // 마트 검색 키워드
    private val _searchKeyword = MutableStateFlow("")
    val searchKeyword: StateFlow<String> = _searchKeyword.asStateFlow()

    /** 마트 이름만 업데이트 (UI 전용) */
    fun onMartChange(value: String) {
        _uiState.update { it.copy(martName = value) }
        _searchKeyword.value = value
        
        // 검색어가 2자 이상일 때 검색 실행
        if (value.length >= 2) {
            searchMarts(value)
        } else {
            _searchResults.value = emptyList()
        }
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
        // 검색 결과 초기화
        _searchResults.value = emptyList()
    }
    
    /** 키워드로 마트 검색 */
    fun searchMarts(keyword: String) {
        viewModelScope.launch {
            try {
                _isSearching.value = true
                
                // 검색 API 호출 - radius는 서버가 처리하도록 기본값(500) 사용
                val searchResults = martSearchUseCase.searchMartByKeyword(keyword, 500)
                
                // 결과를 MartInfo 타입으로 변환
                val martInfoList = searchResults.map { mart ->
                    MartInfo(
                        name = mart.martName,
                        latitude = mart.lat,
                        longitude = mart.lng
                    )
                }
                
                _searchResults.value = martInfoList
            } catch (e: Exception) {
                // 오류 시 빈 결과
                _searchResults.value = emptyList()
            } finally {
                _isSearching.value = false
            }
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
    
    /**
     * 의뢰 등록
     */
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
                Log.d("RequestFormViewModel", "파싱 시도할 사용자 입력 날짜 문자열: ${state.dateTime}")
                
                try {
                    // 사용자 입력 날짜 문자열을 Date 객체로 파싱
                    val userDate = userDateFormat.parse(state.dateTime)
                    if (userDate != null) {
                        Log.d("RequestFormViewModel", "사용자 입력 날짜: ${state.dateTime}, 파싱된 날짜: ${userDate}")
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
                        
                        // API 호출
                        val result = requesterUseCase.postRequest(
                            commission = state.item,
                            commissionCount = maxPeople,
                            commissionPoint = pointPerPerson,
                            commissionRegion = state.martName,
                            commissionDate = isoDateString, // Date 객체 대신 포맷팅된 문자열 사용
                            memberId = 3 // 테스트용 하드코딩 멤버 ID
                        )
                        
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
    
    /**
     * 입력값 유효성 검사
     */
    private fun validateInputs(state: RequestFormUiState): Boolean {
        return state.martName.isNotBlank() && 
               state.maxPeople.isNotBlank() && 
               state.pointPerPerson.isNotBlank() && 
               state.item.isNotBlank() && 
               state.dateTime.isNotBlank() &&
               state.martLat != null &&
               state.martLng != null
    }
    
    /**
     * 요청 상태 초기화
     */
    fun resetRequestState() {
        _requestState.value = RequestState.Initial
    }
    
    /**
     * 검색 결과 초기화
     */
    fun clearSearchResults() {
        _searchResults.value = emptyList()
    }
}

/**
 * 의뢰 등록 요청 상태
 */
sealed class RequestState {
    object Initial : RequestState()
    object Loading : RequestState()
    data class Success(val requestId: Int) : RequestState()
    data class Error(val message: String) : RequestState()
}
