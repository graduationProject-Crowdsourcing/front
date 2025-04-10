package project.graduation.crowd_sourcing.presentation.ui.screen.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.Job

/**
 * 검색 화면의 비즈니스 로직을 처리하는 ViewModel
 * 검색어, 카테고리, 지역 선택 등의 상태 관리
 */
@HiltViewModel
class SearchViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Loading)
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    // 최근 검색어를 저장하는 리스트 (실제로는 Repository나 DataStore를 통해 영구 저장 필요)
    private val _recentSearches = MutableStateFlow<List<String>>(emptyList())
    val recentSearches = _recentSearches.asStateFlow()

    companion object {
        /**
         * 테스트용 카테고리 목록 (가공식품)
         * Domain 계층 구현 시 실제 데이터로 교체 필요
         */
        private val DUMMY_CATEGORIES = listOf(
            "전체", 
            "과자/스낵", 
            "라면/면류", 
            "통조림/캔", 
            "유제품", 
            "냉동식품", 
            "즉석식품", 
            "소스/양념", 
            "음료/커피",
            "쌀/잡곡"
        )
        
        /**
         * 테스트용 지역 목록
         * Domain 계층 구현 시 실제 데이터로 교체 필요
         */
        private val DUMMY_REGIONS = listOf("전체", "동대문구", "강남구", "서초구", "종로구", "용산구")
        
        /**
         * 테스트용 검색 결과 목록 (가공식품)
         * Domain 계층 구현 시 실제 데이터로 교체 필요
         */
        private val DUMMY_SEARCH_RESULTS = listOf(
            SearchResult(
                id = "A001",
                title = "오리온 꼬북칩",
                place = "GS25 강남점",
                remainingDays = 3,
                reward = 5000
            ),
            SearchResult(
                id = "A002",
                title = "농심 신라면",
                place = "CU 종로점",
                remainingDays = 2,
                reward = 4500
            ),
            SearchResult(
                id = "A003",
                title = "동원 참치캔",
                place = "이마트 용산점",
                remainingDays = 5,
                reward = 6000
            ),
            SearchResult(
                id = "A004",
                title = "매일 바나나우유",
                place = "세븐일레븐 서초점",
                remainingDays = 1,
                reward = 4000
            ),
            SearchResult(
                id = "A005",
                title = "청정원 고추장",
                place = "롯데마트 동대문점",
                remainingDays = 4,
                reward = 5500
            ),
            SearchResult(
                id = "A006",
                title = "코카콜라 제로",
                place = "GS25 강남역점",
                remainingDays = 3,
                reward = 4800
            )
        )

        /**
         * 테스트용 최근 검색어
         * 실제로는 API를 통해 가져오거나 로컬 저장소에서 가져와야 함
         */
        private val DUMMY_RECENT_SEARCHES = listOf(
            "Product B",
            "Store C",
            "Store C"
        )

        /**
         * 테스트용 추천 검색어
         * 실제로는 서버에서 가져와야 함
         */
        private val DUMMY_RECOMMENDED_SEARCHES = listOf(
            "Product D",
            "Product D",
            "Store E"
        )
    }

    init {
        loadInitialData()
    }

    /**
     * 초기 데이터를 로드하여 상태를 설정
     * 현재는 더미 데이터를 사용하지만 향후 실제 데이터로 교체 필요
     */
    private fun loadInitialData() {
        viewModelScope.launch {
            try {
                // 최근 검색어 초기화
                _recentSearches.value = DUMMY_RECENT_SEARCHES

                _uiState.value = SearchUiState.Success(
                    searchQuery = "",
                    categories = DUMMY_CATEGORIES,
                    selectedCategory = null,
                    regions = DUMMY_REGIONS,
                    selectedRegion = null,
                    searchResults = emptyList(),
                    recentSearches = DUMMY_RECENT_SEARCHES,
                    recommendedSearches = DUMMY_RECOMMENDED_SEARCHES
                )
            } catch (e: Exception) {
                _uiState.update { 
                    SearchUiState.Error("데이터를 불러오는데 실패했습니다: ${e.message}")
                }
            }
        }
    }
    
    /**
     * 화면이 다시 표시될 때 상태를 새로고침
     * UI 리컴포지션을 위해 사용
     */
    fun refreshState() {
        _uiState.update { currentState ->
            currentState
        }
    }

    /**
     * 검색어를 업데이트
     * 
     * @param query 새로운 검색어
     */
    fun updateSearchQuery(query: String) {
        _uiState.update { currentState ->
            when (currentState) {
                is SearchUiState.Success -> currentState.copy(searchQuery = query)
                else -> currentState
            }
        }
    }

    /**
     * 카테고리를 선택
     * 
     * @param category 선택한 카테고리 (null 또는 "전체"인 경우 '전체' 카테고리 선택)
     */
    fun selectCategory(category: String?) {
        println("ViewModel: 카테고리 선택 - ${category ?: "전체"}")
        _uiState.update { currentState ->
            when (currentState) {
                is SearchUiState.Success -> {
                    val newState = currentState.copy(
                        selectedCategory = if (category == "전체" || category == null) null else category
                    )
                    println("ViewModel: 카테고리 업데이트 후 상태 - ${newState.selectedCategory ?: "전체"}")
                    newState
                }
                else -> currentState
            }
        }
    }

    /**
     * 지역 선택
     * 
     * @param region 선택한 지역 (null 또는 "전체"인 경우 모든 지역 대상)
     */
    fun selectRegion(region: String?) {
        println("ViewModel: 지역 선택 - ${region ?: "전체"}")
        _uiState.update { currentState ->
            when (currentState) {
                is SearchUiState.Success -> {
                    val newState = currentState.copy(
                        selectedRegion = if (region == "전체" || region == null) null else region
                    )
                    println("ViewModel: 지역 업데이트 후 상태 - ${newState.selectedRegion ?: "전체"}")
                    newState
                }
                else -> currentState
            }
        }
    }
    
    /**
     * 검색 실행
     * 검색어, 카테고리, 지역 선택에 따라 결과 필터링
     * @return 필터링된 검색 결과 목록
     */
    fun performSearch(): List<SearchResult> {
        val currentState = _uiState.value
        if (currentState !is SearchUiState.Success) return emptyList()
        
        // 실제로는 API 호출 또는 Repository를 통한 데이터 조회 필요
        // 현재는 더미 데이터로 필터링 로직 구현
        
        val query = currentState.searchQuery.lowercase()
        
        // 검색어가 있을 때만 최근 검색어 목록에 추가
        if (query.isNotEmpty()) {
            addToRecentSearches(currentState.searchQuery)
        }

        val category = currentState.selectedCategory
        val region = currentState.selectedRegion
        
        println("ViewModel: 검색 실행 - 검색어=$query, 카테고리=${category ?: "전체"}, 지역=${region ?: "전체"}")
        
        // 검색어, 카테고리, 지역으로 필터링
        var filteredResults = DUMMY_SEARCH_RESULTS
        
        // 검색어 필터링
        if (query.isNotEmpty()) {
            filteredResults = filteredResults.filter { 
                it.title.lowercase().contains(query) || 
                it.place.lowercase().contains(query)
            }
        }
        
        // 카테고리 필터링 (카테고리가 선택된 경우에만)
        if (category != null) {
            // 실제로는 카테고리별 필터링 로직 필요
            // 더미 데이터에서는 간단한 필터링 구현
            filteredResults = filteredResults.filter {
                when (category) {
                    "과자/스낵" -> it.title.contains("꼬북칩") || it.title.contains("스낵")
                    "라면/면류" -> it.title.contains("라면") || it.title.contains("면")
                    "통조림/캔" -> it.title.contains("참치캔") || it.title.contains("캔")
                    "유제품" -> it.title.contains("우유") || it.title.contains("유제품")
                    "소스/양념" -> it.title.contains("고추장") || it.title.contains("소스")
                    "음료/커피" -> it.title.contains("콜라") || it.title.contains("음료")
                    else -> true
                }
            }
        }
        
        // 지역 필터링 (지역이 선택된 경우에만)
        if (region != null) {
            filteredResults = filteredResults.filter { 
                it.place.contains(region)
            }
        }
        
        println("ViewModel: 필터링된 결과 개수 = ${filteredResults.size}") // 로그 추가
        filteredResults.forEachIndexed { index, result -> // 로그 추가
            println("ViewModel: 필터링 결과 ${index + 1}: $result") // 로그 추가
        }

        // 로컬 UI 상태도 업데이트
        _uiState.update { currentState ->
            when (currentState) {
                is SearchUiState.Success -> currentState.copy(searchResults = filteredResults)
                else -> currentState
            }
        }
        
        // 필터링된 결과 반환
        return filteredResults
    }
    
    /**
     * 최근 검색어 목록에 검색어 추가
     * 동일 검색어가 있을 경우 기존 항목 제거 후 맨 앞에 추가
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
        _uiState.update { currentState ->
            when (currentState) {
                is SearchUiState.Success -> currentState.copy(
                    recentSearches = currentRecent.take(3) // UI에는 최근 3개만 표시
                )
                else -> currentState
            }
        }
    }
    
    /**
     * 추천 검색어로 바로 검색 실행
     */
    fun searchWithTerm(searchTerm: String) {
        _uiState.update { currentState ->
            when (currentState) {
                is SearchUiState.Success -> currentState.copy(searchQuery = searchTerm)
                else -> currentState
            }
        }
        performSearch()
    }

    /**
     * 모든 필터 초기화
     */
    fun resetFilters() {
        _uiState.update { currentState ->
            when (currentState) {
                is SearchUiState.Success -> currentState.copy(
                    searchQuery = "",
                    selectedCategory = null,
                    selectedRegion = null
                )
                else -> currentState
            }
        }
    }

    fun onCategorySelect(category: String) {
        _uiState.update { currentState ->
            when (currentState) {
                is SearchUiState.Success -> currentState.copy(selectedCategory = category)
                else -> currentState
            }
        }
    }

    /**
     * 검색 결과를 직접 업데이트 (외부에서 검색 결과를 받았을 때 사용)
     *
     * @param results 업데이트할 검색 결과 목록
     */
    fun updateSearchResults(results: List<SearchResult>) {
        _uiState.update { currentState ->
            when (currentState) {
                is SearchUiState.Success -> currentState.copy(searchResults = results)
                else -> currentState
            }
        }
    }

    /**
     * 검색 결과와 필터 정보를 함께 업데이트 (외부에서 상태 정보를 받았을 때 사용)
     *
     * @param searchResults 업데이트할 검색 결과 목록
     * @param searchQuery 검색어
     * @param selectedCategory 선택된 카테고리
     * @param selectedRegion 선택된 지역
     */
    fun updateStateWithFilterInfo(
        searchResults: List<SearchResult>,
        searchQuery: String,
        selectedCategory: String?,
        selectedRegion: String?
    ) {
        println("SearchViewModel.updateStateWithFilterInfo 호출됨 - 검색어: '$searchQuery', 카테고리: ${selectedCategory ?: "전체"}, 지역: ${selectedRegion ?: "전체"}, 결과 수: ${searchResults.size}")
        
        val prevState = _uiState.value
        
        _uiState.update { currentState ->
            when (currentState) {
                is SearchUiState.Success -> {
                    val newState = currentState.copy(
                        searchResults = searchResults,
                        searchQuery = searchQuery,
                        selectedCategory = selectedCategory,
                        selectedRegion = selectedRegion
                    )
                    println("SearchViewModel: 상태 업데이트 완료 - 이전(${prevState::class.simpleName}), 이후(Success)")
                    newState
                }
                else -> {
                    println("SearchViewModel: 현재 상태가 Success가 아님(${currentState::class.simpleName}), 상태 변경 불가")
                    currentState
                }
            }
        }
    }
} 