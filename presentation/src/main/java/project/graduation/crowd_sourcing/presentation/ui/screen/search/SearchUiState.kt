package project.graduation.crowd_sourcing.presentation.ui.screen.search

import androidx.annotation.DrawableRes
import project.graduation.crowd_sourcing.presentation.R

/**
 * 검색 화면의 UI 상태를 관리하는 sealed class
 * 앱의 상태에 따라 Loading, Success, Error 상태로 구분됨
 */
sealed class SearchUiState {
    /**
     * 데이터 로딩이 성공적으로 완료된 상태
     * 
     * @param searchQuery 검색어
     * @param selectedCategory 선택된 카테고리 (null이면 전체 선택)
     * @param categories 카테고리 목록
     * @param searchResults 검색 결과 목록
     * @param regions 지역 목록
     * @param selectedRegion 선택된 지역 (null이면 전체 선택)
     */
    data class Success(
        val searchQuery: String = "",
        val selectedCategory: String? = null,
        val categories: List<String> = emptyList(),
        val searchResults: List<SearchResult> = emptyList(),
        val regions: List<String> = emptyList(),
        val selectedRegion: String? = null
    ) : SearchUiState()
    
    /**
     * 오류 발생 상태
     * 
     * @param message 오류 메시지
     */
    data class Error(val message: String) : SearchUiState()
    
    /**
     * 초기 로딩 상태
     */
    object Loading : SearchUiState()
}

/**
 * 검색 결과 데이터 클래스
 * 
 * @param id 제품 고유 ID
 * @param title 제품명
 * @param place 매장명
 * @param remainingDays 남은 일수
 * @param reward 보상금액
 */
data class SearchResult(
    val id: String,
    val title: String,
    val place: String,
    val remainingDays: Int,
    val reward: Int,
    @DrawableRes val icon: Int = R.drawable.ic_list_box // 기본 아이콘 설정
)

/**
 * 정렬 방식 열거형
 */
enum class SortType {
    LATEST, // 최신순
    LOWEST_PRICE_FIRST, // 낮은 가격순
    HIGHEST_PRICE_FIRST, // 높은 가격순
    MOST_POPULAR // 인기순
} 