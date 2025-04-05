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

/**
 * 검색 화면의 비즈니스 로직을 처리하는 ViewModel
 * 검색어, 카테고리, 지역 선택 등의 상태 관리
 */
@HiltViewModel
class SearchViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Loading)
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

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
                _uiState.value = SearchUiState.Success(
                    searchQuery = "",
                    categories = DUMMY_CATEGORIES,
                    selectedCategory = null,
                    regions = DUMMY_REGIONS,
                    selectedRegion = null,
                    searchResults = DUMMY_SEARCH_RESULTS
                )
            } catch (e: Exception) {
                _uiState.update { 
                    SearchUiState.Error("데이터를 불러오는데 실패했습니다: ${e.message}")
                }
            }
        }
    }

    /**
     * 검색어를 업데이트하고 검색 실행
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
        performSearch()
    }

    /**
     * 카테고리를 선택하고 검색 실행
     * 
     * @param category 선택한 카테고리 (null인 경우 '전체' 카테고리 선택)
     */
    fun selectCategory(category: String?) {
        _uiState.update { currentState ->
            when (currentState) {
                is SearchUiState.Success -> currentState.copy(
                    selectedCategory = if (category == "전체") null else category
                )
                else -> currentState
            }
        }
        performSearch()
    }

    /**
     * 지역 선택
     * 
     * @param region 선택한 지역 (null 또는 "전체"인 경우 모든 지역 대상)
     */
    fun selectRegion(region: String?) {
        _uiState.update { currentState ->
            when (currentState) {
                is SearchUiState.Success -> currentState.copy(
                    selectedRegion = if (region == "전체") null else region
                )
                else -> currentState
            }
        }
        performSearch()
    }
    
    /**
     * 검색 실행
     * 검색어, 카테고리, 지역 선택에 따라 결과 필터링
     */
    fun performSearch() {
        val currentState = _uiState.value
        if (currentState !is SearchUiState.Success) return
        
        // 실제로는 API 호출 또는 Repository를 통한 데이터 조회 필요
        // 현재는 더미 데이터로 필터링 로직 구현
        
        val query = currentState.searchQuery.lowercase()
        val category = currentState.selectedCategory
        val region = currentState.selectedRegion
        
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
        
        _uiState.update { 
            (it as SearchUiState.Success).copy(searchResults = filteredResults)
        }
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
        performSearch()
    }
} 