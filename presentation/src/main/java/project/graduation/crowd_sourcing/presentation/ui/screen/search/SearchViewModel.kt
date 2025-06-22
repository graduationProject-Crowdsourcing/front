package project.graduation.crowd_sourcing.presentation.ui.screen.search

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import project.graduation.crowd_sourcing.domain.model.entity.search.CommissionEntity
import project.graduation.crowd_sourcing.domain.usecase.GetSearchHomeInitDataUseCase
import project.graduation.crowd_sourcing.domain.usecase.SearchCommissionUseCase
import javax.inject.Inject
import kotlinx.coroutines.Job
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

/**
 * 검색 화면의 비즈니스 로직을 처리하는 ViewModel
 * 검색어, 카테고리, 지역 선택 등의 상태 관리
 */
@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchCommissionUseCase: SearchCommissionUseCase,
    private val getSearchHomeInitDataUseCase: GetSearchHomeInitDataUseCase
) : ViewModel() {
    // UI 상태
    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Loading)
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    // 최근 검색어를 저장하는 리스트
    private val _recentSearches = MutableStateFlow<List<String>>(emptyList())
    val recentSearches = _recentSearches.asStateFlow()

    // 현재 진행 중인 검색 작업을 취소하기 위한 Job
    private var searchJob: Job? = null
    


    // init 블록 제거 - SearchResultView에서는 초기 로딩이 필요하지 않음

    /**
     * 초기 데이터를 로드하여 상태를 설정
     * 검색 화면에서만 호출되어야 함
     */
    fun loadInitialData() {
        // 이미 Success 상태이고 데이터가 있으면 로드를 건너뜀
        val currentState = _uiState.value
        if (currentState is SearchUiState.Success) {
            println("DEBUG_INIT: 이미 Success 상태로 초기 로드를 건너뜀")
            return
        }
        
        viewModelScope.launch {
            try {
                println("DEBUG_REPO: 초기 검색 데이터 로드 시작")
                val searchHome = getSearchHomeInitDataUseCase()
                
                // 지역과 카테고리 목록에 "전체" 옵션 추가
                val regions = searchHome.regionList
                val categories = searchHome.categoryList
                
                // 최근 검색어 업데이트
                _recentSearches.value = searchHome.recentKeywords
                
                _uiState.value = SearchUiState.Success(
                    searchQuery = "",
                    categories = categories,
                    selectedCategory = null,
                    regions = regions,
                    selectedRegion = null,
                    searchResults = emptyList(),
                    recentSearches = searchHome.recentKeywords,
                    recommendedSearches = searchHome.recommendedKeywords
                )
            } catch (e: Exception) {
                println("DEBUG_REPO: 초기 검색 데이터 예외 발생 - ${e::class.simpleName}: ${e.message}")
                e.printStackTrace()
                
                // 오류 발생 시 기본 데이터로 초기화
                val defaultCategories = listOf("전체", "과자/스낵", "라면/면류", "통조림/캔", "유제품", 
                                      "냉동식품", "즉석식품", "소스/양념", "음료/커피", "쌀/잡곡")
                val defaultRegions = listOf("전체", "동대문구", "강남구", "서초구", "종로구", "용산구")
                
                _uiState.update {
                    SearchUiState.Success(
                        searchQuery = "",
                        categories = defaultCategories,
                        selectedCategory = null,
                        regions = defaultRegions,
                        selectedRegion = null,
                        searchResults = emptyList(),
                        recentSearches = emptyList(),
                        recommendedSearches = emptyList()
                    )
                }
            }
        }
    }

    /**
     * 검색 실행 - 검색어, 카테고리, 지역 선택에 따라 결과 필터링하고 결과를 로드
     * @return 서버에서 로드한 검색 결과 목록
     */
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun performSearch(): List<SearchResult> {
        val currentState = _uiState.value as? SearchUiState.Success ?: return emptyList()

        val query = currentState.searchQuery
        
        // 검색어가 있을 때만 최근 검색어 목록에 추가
        if (query.isNotEmpty()) {
            addToRecentSearches(query)
        }

        // 선택된 필터 값 처리
        val categoryParam = getFilterParam(currentState.selectedCategory)
        val regionParam = getFilterParam(currentState.selectedRegion)
        val sortParam = "createdAt"
        val orderParam = "asc"

        // 서버에서 데이터 가져오기
        try {
            println("DEBUG_SEARCH: 검색 API 호출 시작 - 키워드: '$query', 카테고리: '$categoryParam', 지역: '$regionParam'")
            
            // Flow 대신 직접 List 반환
            val commissions = searchCommissionUseCase(
                searchKeyword = query,
                category = categoryParam,
                region = regionParam,
                sort = sortParam,
                order = orderParam
            )
            
            println("DEBUG_SEARCH: API에서 응답 수신 - 결과 개수: ${commissions.size}")
            
            // 각 Commission 객체 로그 출력
            commissions.forEachIndexed { index, commission ->
                println("DEBUG_SEARCH: Commission[$index] - commission: '${commission.commission}', point: ${commission.commissionpoint}, deadline: ${commission.deadline}")
            }
            
            // Domain Commission 객체를 UI SearchResult 객체로 변환
            val allSearchResults = commissions.map { commission -> 
                convertCommissionToSearchResult(commission)
            }
            
            // 마감된 의뢰 필터링 적용
            val currentState = _uiState.value as? SearchUiState.Success
            val includeExpired = currentState?.includeExpired ?: false
            
            println("DEBUG_FILTER: includeExpired 값: $includeExpired")
            
            val filteredSearchResults = if (includeExpired) {
                println("DEBUG_FILTER: 마감된 의뢰 포함 모드 - 모든 결과 표시")
                allSearchResults
            } else {
                println("DEBUG_FILTER: 마감된 의뢰 제외 모드 - remainingDays != 0 필터링 적용")
                val filtered = allSearchResults.filter { it.remainingDays != 0 }
                println("DEBUG_FILTER: 필터링 전 ${allSearchResults.size}개 -> 필터링 후 ${filtered.size}개")
                // 마감된 의뢰들 로그
                val expiredItems = allSearchResults.filter { it.remainingDays == 0 }
                println("DEBUG_FILTER: 제외된 마감 의뢰 ${expiredItems.size}개:")
                expiredItems.forEachIndexed { index, item ->
                    println("DEBUG_FILTER: 제외[${index}] - id: ${item.id}, title: ${item.title}, remainingDays: ${item.remainingDays}")
                }
                filtered
            }
            
            println("DEBUG_SEARCH: 전체 검색 결과: ${allSearchResults.size}개, 필터링 후: ${filteredSearchResults.size}개 (마감된 의뢰 포함: $includeExpired)")
            
            // 변환된 SearchResult 로그 출력
            filteredSearchResults.forEachIndexed { index, result ->
                println("DEBUG_SEARCH: SearchResult[$index] - id: '${result.id}', title: '${result.title}', reward: ${result.reward}, remainingDays: ${result.remainingDays}")
            }
            
            // UI 상태 업데이트
            updateUiState { state ->
                state.copy(searchResults = filteredSearchResults)
            }
            
            println("DEBUG_SEARCH: 검색 결과 UI 상태 업데이트 완료 - 결과 개수: ${filteredSearchResults.size}")
            
            return filteredSearchResults
        } catch (e: Exception) {
            println("DEBUG_SEARCH: 검색 API 호출 중 오류 발생 - ${e.message}")
            e.printStackTrace()
            return emptyList()
        }
    }

    /**
     * Commission 도메인 객체를 SearchResult UI 객체로 변환
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun convertCommissionToSearchResult(commissionEntity: CommissionEntity): SearchResult {
        val now = LocalDateTime.now()
        val deadline = commissionEntity.deadline
        
        // 디버그 로깅 추가
        println("DEBUG_TIME: 현재 시간: $now, 마감 시간: ${commissionEntity.deadline}")
        println("DEBUG_TIME: 현재 시간 > 마감 시간: ${now.isAfter(deadline)}")
        println("DEBUG_TIME: 마감 시간 > 현재 시간: ${deadline.isAfter(now)}")
        println("DEBUG_TIME: 현재 시간 == 마감 시간: ${now.isEqual(deadline)}")
        
        // 남은 일수 및 시간 계산
        val daysLeft = ChronoUnit.DAYS.between(now, deadline)
        val hoursLeft = ChronoUnit.HOURS.between(now, deadline)
        
        println("DEBUG_TIME: 남은 일수: $daysLeft, 남은 시간: $hoursLeft")
        
        // 마감 시간이 이미 지난 경우
        if (now.isAfter(deadline)) {
            println("DEBUG_TIME: 마감 시간이 이미 지났습니다.")
                    return SearchResult(
            id = commissionEntity.commissionId.toString(),
            title = commissionEntity.commission,
            place = "",
            remainingDays = 0, // 시간이 지났음을 표시하는 특별 값 (0)
            reward = commissionEntity.commissionpoint
        )
        }
        
        // 남은 시간에 따른 표시 방식 결정
        val (displayValue, isHourFormat) = when {
            daysLeft > 0 -> {
                // 하루 이상 남은 경우 일 단위로 표시
                Pair(daysLeft.toInt(), false)
            }
            hoursLeft > 0 -> {
                // 하루 미만 1시간 이상 남은 경우 시간 단위로 표시
                Pair(hoursLeft.toInt(), true)
            }
            else -> {
                // 1시간 미만 남은 경우 최소 1시간으로 표시
                Pair(1, true)
            }
        }
        
        // 일/시간 형식을 구분하기 위해 시간 형식인 경우 음수 값 사용
        val remainingTime = if (isHourFormat) -displayValue else displayValue
        
        println("DEBUG_TIME: 최종 표시 값: $remainingTime (${if (isHourFormat) "시간" else "일"} 단위)")
        
        return SearchResult(
            id = commissionEntity.commissionId.toString(),
            title = commissionEntity.commission,
            place = "",
            remainingDays = remainingTime, // 음수면 시간 단위, 양수면 일 단위
            reward = commissionEntity.commissionpoint
        )
    }

    /**
     * 필터 파라미터 변환 (null, 빈 문자열, "전체" -> "ALL")
     */
    private fun getFilterParam(param: String?): String {
        return if (param.isNullOrBlank() || param == "전체") "ALL" else param
    }

    /**
     * UI 상태 업데이트를 위한 헬퍼 메서드
     */
    private fun updateUiState(update: (SearchUiState.Success) -> SearchUiState.Success) {
        _uiState.update { currentState ->
            when (currentState) {
                is SearchUiState.Success -> update(currentState)
                else -> currentState
            }
        }
    }

    /**
     * 검색어를 업데이트
     */
    fun updateSearchQuery(query: String) {
        updateUiState { it.copy(searchQuery = query) }
    }

    /**
     * 카테고리를 선택
     */
    fun selectCategory(category: String?) {
        updateUiState { 
            it.copy(selectedCategory = if (category == "전체" || category == null) null else category)
        }
    }

    /**
     * 지역 선택
     */
    fun selectRegion(region: String?) {
        updateUiState {
            it.copy(selectedRegion = if (region == "전체" || region == null) null else region)
        }
    }

    /**
     * 추천 검색어로 바로 검색 실행
     */
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun searchWithTerm(searchTerm: String): List<SearchResult> {
        updateUiState { it.copy(searchQuery = searchTerm) }
        return performSearch()
    }

    /**
     * 마감된 의뢰 포함 여부 토글
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun toggleIncludeExpired() {
        val currentState = _uiState.value as? SearchUiState.Success ?: return
        val newIncludeExpired = !currentState.includeExpired
        
        updateUiState { it.copy(includeExpired = newIncludeExpired) }
        
        // 즉시 현재 검색 결과에 필터링 적용
        viewModelScope.launch {
            performSearch()
        }
        
        println("DEBUG_SEARCH: 마감된 의뢰 포함 설정 변경: $newIncludeExpired")
    }

    /**
     * 최근 검색어 목록에 검색어 추가
     */
    private fun addToRecentSearches(query: String) {
        if (query.isBlank()) return

        val currentRecent = _recentSearches.value.toMutableList()
        // 기존에 동일한 검색어가 있으면 제거
        currentRecent.remove(query)
        // 최근 검색어 맨 앞에 추가
        currentRecent.add(0, query)

        // 최대 검색어 개수 제한 (예: 10개)
        if (currentRecent.size > 10) {
            currentRecent.removeAt(currentRecent.size - 1)
        }

        _recentSearches.value = currentRecent

        // UI 상태도 업데이트
        updateUiState { 
            it.copy(recentSearches = currentRecent.take(3)) // UI에는 최근 3개만 표시
        }
    }

    /**
     * 모든 필터 초기화
     */
    fun resetFilters() {
        updateUiState {
            it.copy(
                searchQuery = "",
                selectedCategory = null,
                selectedRegion = null
            )
        }
    }

    /**
     * 검색 결과와 필터 정보를 함께 업데이트 (외부에서 상태 정보를 받았을 때 사용)
     */
    fun updateStateWithFilterInfo(
        searchResults: List<SearchResult>,
        searchQuery: String,
        selectedCategory: String?,
        selectedRegion: String?
    ) {
        
        // 기본 카테고리와 지역 목록 설정
        val defaultCategories = listOf("전체", "과자/스낵", "라면/면류", "통조림/캔", "유제품", 
                                      "냉동식품", "즉석식품", "소스/양념", "음료/커피", "쌀/잡곡")
        val defaultRegions = listOf("전체", "동대문구", "강남구", "서초구", "종로구", "용산구")
        
        // 기본적으로 마감된 의뢰는 포함하지 않으므로 필터링 적용
        val includeExpired = false
        val filteredSearchResults = if (includeExpired) {
            searchResults
        } else {
            searchResults.filter { it.remainingDays != 0 } // remainingDays가 0이면 마감된 것
        }
        
        println("DEBUG_UPDATE_STATE: 받은 검색 결과: ${searchResults.size}개, 필터링 후: ${filteredSearchResults.size}개 (includeExpired: $includeExpired)")
        
        _uiState.value = SearchUiState.Success(
            searchQuery = searchQuery,
            categories = defaultCategories,
            selectedCategory = selectedCategory,
            regions = defaultRegions,
            selectedRegion = selectedRegion,
            searchResults = filteredSearchResults,
            recentSearches = emptyList(),
            recommendedSearches = emptyList(),
            includeExpired = includeExpired
        )
        
        println("DEBUG_UPDATE_STATE: 상태 업데이트 완료 - 검색어: '$searchQuery', 카테고리: ${selectedCategory ?: "전체"}, 지역: ${selectedRegion ?: "전체"}, 결과 개수: ${filteredSearchResults.size}, includeExpired: $includeExpired")
    }

    /**
     * 현재 설정된 필터로 검색을 새로고침
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun refreshSearch() {
        viewModelScope.launch {
            performSearch()
        }
    }
} 