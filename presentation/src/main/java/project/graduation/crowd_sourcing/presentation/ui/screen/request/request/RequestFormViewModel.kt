package project.graduation.crowd_sourcing.presentation.ui.screen.request.request

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import project.graduation.crowd_sourcing.domain.local.TokenManager
import project.graduation.crowd_sourcing.domain.model.entity.martsearch.MartEntity
import project.graduation.crowd_sourcing.domain.usecase.GetSearchHomeInitDataUseCase
import project.graduation.crowd_sourcing.domain.usecase.MartSearchUseCase
import project.graduation.crowd_sourcing.domain.usecase.RequesterUseCase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject
import retrofit2.HttpException


@HiltViewModel
class RequestFormViewModel @Inject constructor(
    private val requesterUseCase: RequesterUseCase,
    private val tokenManager: TokenManager,
    private val martSearchUseCase: MartSearchUseCase,
    private val getSearchHomeInitDataUseCase: GetSearchHomeInitDataUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(RequestFormUiState())
    val uiState: StateFlow<RequestFormUiState> = _uiState

    init {
        loadCategoryList()

        savedStateHandle.get<List<MartEntity>>("selectedMarts")?.let { marts ->
            selectedMarts = marts.map { it.martName }
            setMartList(marts)
        }

        savedStateHandle.get<List<String>>("selectedMarts_prefill")?.let { martNames ->
            updateSelectedMarts(martNames)
        }
    }

    // 선택된 지역 리스트
    var selectedRegion by mutableStateOf("")


    // 선택된 마트 리스트
    var selectedMarts by mutableStateOf<List<String>>(emptyList())
        private set

    // 의뢰 등록 상태
    private val _requestState = MutableStateFlow<RequestState>(RequestState.Initial)
    val requestState: StateFlow<RequestState> = _requestState

    // 마트 리스트 상태
    private val _martList = MutableStateFlow<List<MartEntity>>(emptyList())

    // 카테고리 리스트 상태
    private val _categoryList = MutableStateFlow<List<String>>(emptyList())
    val categoryList: StateFlow<List<String>> = _categoryList.asStateFlow()

    fun updateSelectedRegion(region: String) {
        selectedRegion = region
        _uiState.update { it.copy(sigungu = region) }

        // 선택된 지역 기준으로 마트 리스트 불러오기
        viewModelScope.launch {
            try {
                val marts = martSearchUseCase.getMartList(region).getOrThrow()
                _martList.value = marts
            } catch (e: Exception) {
                Log.e("RequestFormViewModel", "마트 리스트 불러오기 실패: $region", e)
                _martList.value = emptyList()
            }
        }
    }

    fun updateSelectedMarts(martNames: List<String>) {
        selectedMarts = martNames
    }

    fun setMartList(marts: List<MartEntity>) {
        _martList.value = marts
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

    fun onCategorySelected(value: String) {
        _uiState.update { it.copy(selectedCategory = value) }
    }

    fun onExpirationDateChange(value: String) {
        _uiState.update { it.copy(expirationDate = value) }
    }


    // 의뢰 생성
    @RequiresApi(Build.VERSION_CODES.O)
    fun submitRequest() {
        Log.d("SubmitRequest", "submitRequest() 호출됨")

        val state = _uiState.value
        Log.d("SubmitRequest", "현재 UI 상태: $state")

        // 1) 필수 입력값이 비어 있는지 확인
        if (!validateInputs(state)) {
            Log.d("SubmitRequest", "입력값 유효성 검사 실패")
            _requestState.value = RequestState.Error("모든 필드를 입력해주세요.")
            return
        }

        viewModelScope.launch {
            _requestState.value = RequestState.Loading
            Log.d("SubmitRequest", "로딩 상태로 전환됨")

            try {
                // 2) 숫자 값 변환 (문자열 → Int)
                val maxPeople = state.maxPeople.toIntOrNull() ?: 0
                val pointPerPerson = state.pointPerPerson.toIntOrNull() ?: 0

                // 3) 사용자 입력 날짜 파싱 (yyyy-MM-dd HH:mm → Date)
                val userDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                userDateFormat.timeZone = TimeZone.getDefault() // 로컬 기준

                val userDate = userDateFormat.parse(state.expirationDate)

                // 4) 날짜 유효성 검사: null이거나 과거 시간이면 오류
                if (userDate == null || userDate.before(Date())) {
                    Log.d("SubmitRequest", "날짜 유효성 실패: ${state.expirationDate}")
                    _requestState.value = RequestState.Error("마감 시간은 현재 시간 이후로 설정해야 합니다.")
                    return@launch
                }

                // 5) Date → ISO 8601 문자열 포맷 변환 (Z: UTC 기준)
                val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                isoFormat.timeZone = TimeZone.getTimeZone("UTC")
                val expirationDate = isoFormat.format(userDate) // 마감 시간

                // 6) 현재 시간 → ISO8601 포맷 (workDate)
                val workDate = isoFormat.format(Date())

                // 7) 사용자 ID 가져오기
                val userId = tokenManager.getUserId()
                if (userId == -1) {
                    _requestState.value = RequestState.Error("로그인이 필요합니다. 다시 로그인해 주세요.")
                    return@launch
                }
                Log.d("SubmitRequest", "userId=$userId")
                Log.d("SubmitRequest", "selectedMarts=${selectedMarts}")

                // 마트 리스트 전체에 대해 반복 호출
                val selectedMartNames = selectedMarts

                if (selectedMartNames.isEmpty()) {
                    Log.d("SubmitRequest", "선택한 마트 없음")
                    _requestState.value = RequestState.Error("선택한 마트가 없습니다.")
                    return@launch
                }

                var lastResult: Result<Int>? = null

                for (martName in selectedMartNames) {

                    val result = requesterUseCase.postWork(
                        work = "${state.sigungu} - ${state.item}",
                        workCount = maxPeople,
                        workpoint = pointPerPerson,
                        martNames = listOf(martName),
                        sigungu = state.sigungu,
                        item = state.item,
                        workDate = workDate,
                        memberId = userId,
                        category = state.selectedCategory,
                        workhour = 2,
                        expirationDate = expirationDate
                    )
                    lastResult = result
                    Log.d("SubmitRequest", "API 응답 결과: $result")

                    val exception = result.exceptionOrNull()
                    if (exception is retrofit2.HttpException) {
                        Log.d("SubmitRequest", "HTTP 응답 코드: ${exception.code()}")
                        Log.d("SubmitRequest", "응답 메시지: ${exception.response()?.errorBody()?.string()}")
                    }
                }

                _requestState.value = RequestState.Success(lastResult ?: Result.failure(Exception("등록 실패")))

            } catch (e: Exception) {
                Log.e("RequestFormViewModel", "의뢰 등록 오류", e)
                val errorMessage = when {
                    e.message?.contains("403") == true -> "권한이 없습니다. 로그인 세션이 만료되었을 수 있습니다."
                    e.message?.contains("401") == true -> "인증 오류가 발생했습니다. 다시 로그인해 주세요."
                    e.message?.contains("400") == true -> "입력값이 올바르지 않습니다."
                    e.message?.contains("500") == true -> "서버 오류가 발생했습니다. 잠시 후 다시 시도해 주세요."
                    e.message?.contains("timeout") == true -> "서버 연결 시간이 초과되었습니다."
                    else -> "의뢰 등록 중 오류가 발생했습니다: ${e.message ?: "알 수 없는 오류"}"
                }
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
                state.expirationDate.isNotBlank() &&
                state.selectedCategory.isNotBlank()
    }

    // 요청 상태 초기화
    fun resetRequestState() {
        _requestState.value = RequestState.Initial
    }

    // 카테고리 초기 데이터 로딩
    fun loadCategoryList() {
        viewModelScope.launch {
            try {
                val searchHome = getSearchHomeInitDataUseCase()
                _categoryList.value = searchHome.categoryList
                Log.d("RequestFormViewModel", "카테고리 로딩 완료: ${searchHome.categoryList}")
            } catch (e: Exception) {
                Log.e("RequestFormViewModel", "카테고리 로딩 실패", e)
            }
        }
    }
}

// 의뢰 등록 요청 상태
sealed class RequestState {
    object Initial : RequestState()
    object Loading : RequestState()
    data class Success(val requestId: Result<Int>) : RequestState()
    data class Error(val message: String) : RequestState()
}
