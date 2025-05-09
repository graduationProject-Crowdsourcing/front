package project.graduation.crowd_sourcing.presentation.ui.screen.search

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import project.graduation.crowd_sourcing.domain.model.entity.Commission
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

    init {
        loadInitialData()
    }

    /**
     * 초기 데이터를 로드하여 상태를 설정
     */
    private fun loadInitialData() {
        viewModelScope.launch {
            try {
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
        val results = mutableListOf<SearchResult>()
        
        try {
            searchCommissionUseCase(
                searchKeyword = query,
                category = categoryParam,
                region = regionParam,
                sort = sortParam,
                order = orderParam
            ).collect { commissions ->
                // Domain Commission 객체를 UI SearchResult 객체로 변환
                val searchResults = commissions.map { commission -> 
                    convertCommissionToSearchResult(commission)
                }
                
                results.addAll(searchResults)
                
                // UI 상태 업데이트
                updateUiState { state ->
                    state.copy(searchResults = searchResults)
                }
            }
            
            return results
        } catch (e: Exception) {
            return emptyList()
        }
    }

    /**
     * Commission 도메인 객체를 SearchResult UI 객체로 변환
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun convertCommissionToSearchResult(commission: Commission): SearchResult {
        val now = LocalDateTime.now()
        val deadline = commission.deadline
        
        // 남은 일수 및 시간 계산
        val daysLeft = ChronoUnit.DAYS.between(now, deadline)
        val hoursLeft = ChronoUnit.HOURS.between(now, deadline)
        
        // 남은 시간이 24시간 미만이면 시간으로, 그 이상이면 일수로 표시
        val remainingDays = if (daysLeft < 1) {
            // 만약 시간이 1시간 미만이면 최소 1시간으로 표시
            if (hoursLeft < 1) 1 else hoursLeft.toInt()
        } else {
            daysLeft.toInt()
        }
        
        // 시간 단위인지 일 단위인지 구분 (시간 단위 표시를 위해 음수 값 사용)
        val timeUnit = if (daysLeft < 1) -1 else 1

        return SearchResult(
            id = commission.commission,
            title = commission.commission,
            place = "",
            remainingDays = remainingDays * timeUnit, // 음수면 시간 단위, 양수면 일 단위
            reward = commission.commissionPoint
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
        updateUiState {
            it.copy(
                searchResults = searchResults,
                searchQuery = searchQuery,
                selectedCategory = selectedCategory,
                selectedRegion = selectedRegion
            )
        }
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