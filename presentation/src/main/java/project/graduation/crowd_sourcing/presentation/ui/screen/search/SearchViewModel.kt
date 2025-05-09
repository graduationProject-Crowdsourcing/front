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
    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadInitialData() {
        viewModelScope.launch {
            try {
                println("DEBUG: 서버에서 초기 검색 데이터 로드 시작")
                
                // 서버에서 초기 검색 데이터 로드
                val searchHome = getSearchHomeInitDataUseCase()
                
                println("DEBUG: 초기 검색 데이터 로드 완료 - 지역: ${searchHome.regionList.size}개, 카테고리: ${searchHome.categoryList.size}개")
                println("DEBUG: 최근 검색어: ${searchHome.recentKeywords.size}개, 추천 검색어: ${searchHome.recommendedKeywords.size}개")
                
                // 지역과 카테고리 목록에 "전체" 옵션 추가
                val regions = listOf("전체") + searchHome.regionList
                val categories = listOf("전체") + searchHome.categoryList
                
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

                println("DEBUG: 초기 데이터 로드 완료 - UI 상태 업데이트")
            } catch (e: Exception) {
                println("DEBUG: 초기 데이터 로드 중 오류 발생 - ${e.javaClass.simpleName}: ${e.message}")
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
                
                println("DEBUG: 오류로 인해 기본 데이터로 초기화 완료")
            }
        }
    }

    /**
     * 서버로부터 Commission 데이터 로드
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadCommissionsFromServer() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            try {
                val currentState = _uiState.value
                if (currentState !is SearchUiState.Success) {
                    println("DEBUG: loadCommissionsFromServer - 현재 상태가 Success가 아님(${currentState::class.simpleName})")
                    return@launch
                }

                val searchKeyword = currentState.searchQuery.trim()
                
                // 선택된 필터 값 처리
                // "전체"가 선택되었거나 null인 경우 "ALL" 사용, 그 외에는 실제 선택값 사용
                val category = if (currentState.selectedCategory.isNullOrBlank() || currentState.selectedCategory == "전체") {
                    "ALL"
                } else {
                    currentState.selectedCategory
                }
                
                val region = if (currentState.selectedRegion.isNullOrBlank() || currentState.selectedRegion == "전체") {
                    "ALL"
                } else {
                    currentState.selectedRegion
                }
                
                val sort = "createdAt"  // API 명세에 따른 기본값으로 설정
                val order = "asc"       // API 명세에 따른 기본값으로 설정

                println("DEBUG: 검색 API 호출 시작 - 키워드: '$searchKeyword', 카테고리: '$category', 지역: '$region', 정렬: $sort $order")

                searchCommissionUseCase(
                    searchKeyword = searchKeyword,
                    category = category,
                    region = region,
                    sort = sort,
                    order = order
                ).collect { commissions ->
                    // 응답 로그
                    println("DEBUG: searchCommissionUseCase 응답 수신 - 데이터 개수: ${commissions.size}")
                    commissions.forEachIndexed { index, commission ->
                        println("DEBUG: Commission[$index] - commission: ${commission.commission}, point: ${commission.commissionPoint}, deadline: ${commission.deadline}")
                    }

                    // Domain Commission 객체를 UI SearchResult 객체로 변환
                    val searchResults = commissions.map { commission ->
                        val now = LocalDateTime.now()
                        val daysLeft = ChronoUnit.DAYS.between(now, commission.deadline).toInt()

                        val searchResult = SearchResult(
                            id = commission.commission,  // todo 실제 commision id로 변경하기기
                            title = commission.commission,
                            place = "서버 데이터",  // todo 실제 위치로 변경하기기
                            remainingDays = if (daysLeft > 0) daysLeft else 1,
                            reward = commission.commissionPoint
                        )
                        println("DEBUG: SearchResult 변환 - id: ${searchResult.id}, title: ${searchResult.title}, place: ${searchResult.place}, days: ${searchResult.remainingDays}, reward: ${searchResult.reward}")
                        searchResult
                    }

                    println("DEBUG: UI 상태 업데이트 - 결과 개수: ${searchResults.size}")

                    // UI 상태 업데이트
                    _uiState.update { state ->
                        when (state) {
                            is SearchUiState.Success -> {
                                val newState = state.copy(searchResults = searchResults)
                                println("DEBUG: 상태 업데이트 완료 - 이전 결과 개수: ${state.searchResults.size}, 이후 결과 개수: ${newState.searchResults.size}")
                                newState
                            }
                            else -> {
                                println("DEBUG: 상태 업데이트 실패 - 현재 상태가 Success가 아님(${state::class.simpleName})")
                                state
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                println("DEBUG: 검색 중 오류 발생 - ${e.javaClass.simpleName}: ${e.message}")
                e.printStackTrace()
                
                _uiState.update { currentState ->
                    when (currentState) {
                        is SearchUiState.Success -> {
                            println("DEBUG: 오류로 인한 상태 업데이트 - 검색 결과 초기화")
                            currentState.copy(searchResults = emptyList())
                        }
                        else -> {
                            println("DEBUG: 오류 상태로 변경")
                            SearchUiState.Error("검색에 실패했습니다: ${e.message}")
                        }
                    }
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
            println("DEBUG: 상태 새로고침")
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
        println("DEBUG: 카테고리 선택 - ${category ?: "전체"}")
        _uiState.update { currentState ->
            when (currentState) {
                is SearchUiState.Success -> {
                    val newState = currentState.copy(
                        selectedCategory = if (category == "전체" || category == null) null else category
                    )
                    println("DEBUG: 카테고리 업데이트 완료 - ${newState.selectedCategory ?: "전체"}")
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
        println("DEBUG: 지역 선택 - ${region ?: "전체"}")
        _uiState.update { currentState ->
            when (currentState) {
                is SearchUiState.Success -> {
                    val newState = currentState.copy(
                        selectedRegion = if (region == "전체" || region == null) null else region
                    )
                    println("DEBUG: 지역 업데이트 완료 - ${newState.selectedRegion ?: "전체"}")
                    newState
                }
                else -> currentState
            }
        }
    }

    /**
     * 검색 실행
     * 검색어, 카테고리, 지역 선택에 따라 결과 필터링하고 비동기적으로 결과를 로드
     * @return 서버에서 로드한 검색 결과 목록
     */
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun performSearch(): List<SearchResult> {
        val currentState = _uiState.value
        if (currentState !is SearchUiState.Success) {
            println("DEBUG: performSearch - 현재 상태가 Success가 아님(${currentState::class.simpleName})")
            return emptyList()
        }

        val query = currentState.searchQuery
        println("DEBUG: performSearch 호출됨 - 검색어: '$query'")

        // 검색어가 있을 때만 최근 검색어 목록에 추가
        if (query.isNotEmpty()) {
            addToRecentSearches(query)
            println("DEBUG: 최근 검색어에 '$query' 추가됨")
        }

        val category = currentState.selectedCategory
        val region = currentState.selectedRegion

        println("DEBUG: 검색 실행 - 검색어=$query, 카테고리=${category ?: "전체"}, 지역=${region ?: "전체"}")

        // 선택된 필터 값 처리
        val categoryParam = if (category.isNullOrBlank() || category == "전체") "ALL" else category
        val regionParam = if (region.isNullOrBlank() || region == "전체") "ALL" else region
        val sortParam = "createdAt"
        val orderParam = "asc"

        // 서버에서 데이터 가져오기 (suspend 함수로 실제 결과 기다림)
        val results = mutableListOf<SearchResult>()
        
        try {
            println("DEBUG: 검색 API 호출 시작 (동기 방식) - 키워드: '$query', 카테고리: '$categoryParam', 지역: '$regionParam'")
            
            searchCommissionUseCase(
                searchKeyword = query,
                category = categoryParam,
                region = regionParam,
                sort = sortParam,
                order = orderParam
            ).collect { commissions ->
                println("DEBUG: searchCommissionUseCase 응답 수신 - 데이터 개수: ${commissions.size}")
                
                // Domain Commission 객체를 UI SearchResult 객체로 변환
                val searchResults = commissions.map { commission ->
                    val now = LocalDateTime.now()
                    val daysLeft = ChronoUnit.DAYS.between(now, commission.deadline).toInt()

                    SearchResult(
                        id = commission.commission,
                        title = commission.commission,
                        place = "서버 데이터",
                        remainingDays = if (daysLeft > 0) daysLeft else 1,
                        reward = commission.commissionPoint
                    )
                }
                
                results.addAll(searchResults)
                
                // UI 상태 업데이트
                _uiState.update { state ->
                    when (state) {
                        is SearchUiState.Success -> {
                            state.copy(searchResults = searchResults)
                        }
                        else -> state
                    }
                }
                
                println("DEBUG: 검색 결과 변환 및 상태 업데이트 완료 - 결과 개수: ${results.size}")
            }
            
            return results
            
        } catch (e: Exception) {
            println("DEBUG: 검색 중 오류 발생 - ${e.javaClass.simpleName}: ${e.message}")
            e.printStackTrace()
            return emptyList()
        }
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
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun searchWithTerm(searchTerm: String): List<SearchResult> {
        _uiState.update { currentState ->
            when (currentState) {
                is SearchUiState.Success -> currentState.copy(searchQuery = searchTerm)
                else -> currentState
            }
        }
        return performSearch()
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

    /**
     * 현재 설정된 필터로 검색을 새로고침
     * 결과 화면에서 호출될 때 사용
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun refreshSearch() {
        val currentState = _uiState.value
        if (currentState !is SearchUiState.Success) {
            println("DEBUG: refreshSearch - 현재 상태가 Success가 아님(${currentState::class.simpleName})")
            return
        }
        
        println("DEBUG: refreshSearch - 현재 필터로 검색 실행 - 검색어: '${currentState.searchQuery}', 카테고리: ${currentState.selectedCategory ?: "전체"}, 지역: ${currentState.selectedRegion ?: "전체"}")
        loadCommissionsFromServer()
    }
} 