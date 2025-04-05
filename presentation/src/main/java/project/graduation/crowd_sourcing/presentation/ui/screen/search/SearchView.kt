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
import project.graduation.crowd_sourcing.presentation.ui.screen.search.component.SearchContentSection
import project.graduation.crowd_sourcing.presentation.ui.screen.search.component.SearchResultSection

/**
 * 검색 화면 UI 구성
 * 
 * 주요 기능:
 * 1. 검색어 입력
 * 2. 카테고리 선택
 * 3. 지역 선택
 * 4. 검색 결과 표시
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchView() {
    val viewModel: SearchViewModel = hiltViewModel()
    val uiState = viewModel.uiState.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    
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
                            SearchContentSection(
                                state = state,
                                viewModel = viewModel,
                                keyboardController = keyboardController,
                                focusManager = focusManager
                            )
                            
                            // 검색 결과 섹션
                            if (state.searchResults.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                SearchResultSection(
                                    title = "최근 검색",
                                    searchResults = state.searchResults.take(3),
                                    onItemClick = { /* 아이템 클릭 처리 */ }
                                )
                            }
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