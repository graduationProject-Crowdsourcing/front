package project.graduation.crowd_sourcing.presentation.ui.screen.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import project.graduation.crowd_sourcing.presentation.ui.navigation.Screen
import project.graduation.crowd_sourcing.presentation.ui.screen.search.component.SearchInputSection
import project.graduation.crowd_sourcing.presentation.ui.screen.search.component.SelectedFiltersSection
import project.graduation.crowd_sourcing.presentation.ui.screen.search.component.SearchButtonSection
import project.graduation.crowd_sourcing.presentation.ui.screen.search.component.RecentSearchesSection
import project.graduation.crowd_sourcing.presentation.ui.screen.search.component.RecommendedSearchesSection

/**
 * 검색 화면 UI 구성
 * 
 * 주요 기능:
 * 1. 검색어 입력
 * 2. 필터 선택 정보 표시 (지역/카테고리)
 * 3. 검색 버튼
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchView(
    navController: NavController
) {
    val viewModel: SearchViewModel = hiltViewModel()
    val uiState = viewModel.uiState.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()
    
    // 화면 콘텐츠 표시 여부를 제어하는 상태
    val showContent = remember { mutableStateOf(true) }
    
    // 화면 생명주기에 따라 콘텐츠 관리
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> {
                    // 화면이 백그라운드로 이동할 때 모든 콘텐츠를 숨김
                    showContent.value = false
                }
                Lifecycle.Event.ON_RESUME -> {
                    // 화면이 포그라운드로 돌아올 때 모든 콘텐츠를 다시 표시
                    showContent.value = true
                    
                    // FilterSelectionView에서 돌아올 때 SavedStateHandle에서 필터 상태 복원
                    navController.currentBackStackEntry?.savedStateHandle?.let { savedStateHandle ->
                        // 선택된 카테고리 복원
                        savedStateHandle.get<String?>("selectedCategory")?.let { category ->
                            println("SearchView: savedStateHandle에서 카테고리 복원 - $category")
                            if (category == "전체") {
                                viewModel.selectCategory(null)  // "전체"는 null로 처리
                            } else {
                                viewModel.selectCategory(category)
                            }
                            // 사용 후 제거
                            savedStateHandle.remove<String?>("selectedCategory")
                        }
                        
                        // 선택된 지역 복원
                        savedStateHandle.get<String?>("selectedRegion")?.let { region ->
                            println("SearchView: savedStateHandle에서 지역 복원 - $region")
                            if (region == "전체") {
                                viewModel.selectRegion(null)  // "전체"는 null로 처리
                            } else {
                                viewModel.selectRegion(region)
                            }
                            // 사용 후 제거
                            savedStateHandle.remove<String?>("selectedRegion")
                        }
                    }
                }
                else -> {}
            }
        }
        
        lifecycleOwner.lifecycle.addObserver(observer)
        
        onDispose {
            // 컴포지션이 해제될 때 옵저버 제거
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)
    ) {
        when (val state = uiState.value) {
            is SearchUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is SearchUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = state.message)
                }
            }
            is SearchUiState.Success -> {
                // showContent 상태에 따라 모든 콘텐츠 조건부 렌더링
                if (showContent.value) {
                    // 검색 내용 (스크롤 가능)
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        item {
                            // 검색 입력 섹션
                            SearchInputSection(
                                searchQuery = state.searchQuery,
                                onSearchQueryChange = { viewModel.updateSearchQuery(it) },
                                keyboardController = keyboardController,
                                focusManager = focusManager
                            )
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            // 선택된 필터 표시 섹션 (지역, 카테고리)
                            SelectedFiltersSection(
                                selectedCategory = state.selectedCategory,
                                selectedRegion = state.selectedRegion,
                                onFilterClick = { 
                                    // 현재 상태를 필터 선택 화면으로 전달
                                    navController.currentBackStackEntry?.savedStateHandle?.set(
                                        "initialCategory", state.selectedCategory
                                    )
                                    navController.currentBackStackEntry?.savedStateHandle?.set(
                                        "initialRegion", state.selectedRegion
                                    )
                                    
                                    // 필터 선택 화면으로 이동
                                    navController.navigate(Screen.FilterSelectionScreen.route)
                                }
                            )
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            // 검색 버튼 섹션
                            SearchButtonSection(
                                onSearchClick = {
                                    keyboardController?.hide()
                                    focusManager.clearFocus()
                                    
                                    // 검색 실행 및 결과 얻기
                                    val searchResults = viewModel.performSearch()
                                    
                                    // 현재 상태 가져오기 및 조건 확인
                                    val currentState = uiState.value as? SearchUiState.Success
                                    if (currentState != null) {
                                        // 검색 결과 및 필터 정보를 savedStateHandle에 저장
                                        navController.currentBackStackEntry?.savedStateHandle?.apply {
                                            set("searchResults", searchResults.toTypedArray())
                                            set("searchQuery", currentState.searchQuery)
                                            set("selectedCategory", currentState.selectedCategory)
                                            set("selectedRegion", currentState.selectedRegion)
                                            
                                            // 로그 추가: 저장되는 데이터 확인
                                            println("SearchView: savedStateHandle에 저장 - 검색어: ${currentState.searchQuery}, 카테고리: ${currentState.selectedCategory ?: "전체"}, 지역: ${currentState.selectedRegion ?: "전체"}")
                                        }
                                        
                                        // 검색 결과 화면으로 이동
                                        navController.navigate(Screen.SearchResultScreen.route)
                                    }
                                }
                            )
                            
                            Spacer(modifier = Modifier.height(24.dp))
                            
                            // 최근 검색어 섹션
                            RecentSearchesSection(
                                recentSearches = state.recentSearches,
                                onSearchTermClick = { searchTerm ->
                                    keyboardController?.hide()
                                    focusManager.clearFocus()
                                    viewModel.searchWithTerm(searchTerm)
                                    navController.navigate(Screen.SearchResultScreen.route)
                                }
                            )
                            
                            Spacer(modifier = Modifier.height(24.dp))
                            
                            // 추천 검색어 섹션
                            RecommendedSearchesSection(
                                recommendedSearches = state.recommendedSearches,
                                onSearchTermClick = { searchTerm ->
                                    keyboardController?.hide()
                                    focusManager.clearFocus()
                                    viewModel.searchWithTerm(searchTerm)
                                    navController.navigate(Screen.SearchResultScreen.route)
                                }
                            )
                        }
                    }
                } else {
                    // 화면 전환 중일 때는 빈 화면 표시
                    Box(modifier = Modifier.fillMaxSize())
                }
            }
        }
    }
} 