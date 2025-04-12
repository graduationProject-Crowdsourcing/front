package project.graduation.crowd_sourcing.presentation.ui.screen.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import project.graduation.crowd_sourcing.presentation.ui.screen.search.component.FilterChip
import androidx.lifecycle.SavedStateHandle
import androidx.compose.runtime.LaunchedEffect

/**
 * 검색 결과 화면
 * 
 * @param navController 네비게이션 컨트롤러
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchResultView(
    navController: NavController
) {
    val viewModel: SearchViewModel = hiltViewModel()
    val uiState = viewModel.uiState.collectAsState()
    
    // 검색 결과를 저장할 상태
    var searchResults by remember { mutableStateOf<List<SearchResult>>(emptyList()) }
    
    // 화면이 표시될 때 savedStateHandle에서 검색 결과 및 필터 정보 가져오기
    LaunchedEffect(Unit) {
        navController.previousBackStackEntry?.savedStateHandle?.let { handle ->
            // 객체 존재 여부 확인 로그
            println("SearchResultView: previousBackStackEntry 존재, savedStateHandle 존재")
            
            // 검색 결과 가져오기
            val resultsArray = handle.get<Array<SearchResult>>("searchResults")
            println("SearchResultView: searchResults 배열 ${if (resultsArray != null) "존재 (${resultsArray.size}개)" else "없음"}")
            
            resultsArray?.let {
                searchResults = it.toList()
                println("SearchResultView: 검색 결과를 리스트로 변환 완료, 크기: ${searchResults.size}")
            }
            
            // 검색어, 카테고리, 지역 정보 가져오기
            val searchQuery = handle.get<String>("searchQuery") ?: ""
            val selectedCategory = handle.get<String?>("selectedCategory")
            val selectedRegion = handle.get<String?>("selectedRegion")
            
            println("SearchResultView: savedStateHandle에서 필터 정보 로드 - 검색어: '$searchQuery', 카테고리: ${selectedCategory ?: "전체"}, 지역: ${selectedRegion ?: "전체"}")
            
            // ViewModel 상태 전체 업데이트
            viewModel.updateStateWithFilterInfo(
                searchResults = searchResults,
                searchQuery = searchQuery,
                selectedCategory = selectedCategory,
                selectedRegion = selectedRegion
            )
            
            println("SearchResultView: viewModel.updateStateWithFilterInfo 호출 완료")
        } ?: println("SearchResultView: previousBackStackEntry 또는 savedStateHandle이 null")
    }
    
    // 필터 바텀 시트 상태
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    
    // 현재 선택된 정렬 방식
    var sortType by remember { mutableStateOf(SortType.LATEST) }
    
    // 정렬된 검색 결과
    val sortedResults = remember(searchResults, sortType) {
        when (sortType) {
            SortType.LOWEST_PRICE_FIRST -> searchResults.sortedBy { it.reward }
            SortType.HIGHEST_PRICE_FIRST -> searchResults.sortedByDescending { it.reward }
            SortType.LATEST -> searchResults // 최신순은 이미 정렬되어 있다고 가정
            SortType.MOST_POPULAR -> searchResults.sortedByDescending { it.reward } // 인기순은 일단 높은 가격순으로 대체
            // 또는 else -> searchResults 로 처리 가능
        }
    }
    
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // 검색 필터 영역
        SearchResultFilterBar(
            state = uiState.value,
            onFilterClick = { showBottomSheet = true }
        )
        
        // 검색 결과 목록
        if (searchResults.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("검색 결과가 없습니다.")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(sortedResults) { result ->
                    SearchResultItem(result = result)
                    Divider()
                }
            }
        }
    }
    
    // 필터링 바텀 시트는 Column과 별개로 관리 (Scaffold 외부)
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "정렬 기준",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                SortOption(
                    title = "낮은 가격순",
                    isSelected = sortType == SortType.LOWEST_PRICE_FIRST,
                    onClick = { sortType = SortType.LOWEST_PRICE_FIRST }
                )
                
                SortOption(
                    title = "높은 가격순",
                    isSelected = sortType == SortType.HIGHEST_PRICE_FIRST,
                    onClick = { sortType = SortType.HIGHEST_PRICE_FIRST }
                )
                
                SortOption(
                    title = "최신순",
                    isSelected = sortType == SortType.LATEST,
                    onClick = { sortType = SortType.LATEST }
                )
                
                SortOption(
                    title = "인기순",
                    isSelected = sortType == SortType.MOST_POPULAR,
                    onClick = { sortType = SortType.MOST_POPULAR }
                )
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(onClick = { showBottomSheet = false }) {
                        Text("취소")
                    }
                    
                    Button(
                        onClick = { 
                            // sortType에 따라 결과가 이미 정렬됨 (sortedResults에 의해)
                            showBottomSheet = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF1785E4)
                        )
                    ) {
                        Text("적용")
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

/**
 * 검색 결과 필터 바
 */
@Composable
fun SearchResultFilterBar(
    state: SearchUiState,
    onFilterClick: () -> Unit
) {
    // UI 상태 로그 추가
    println("SearchResultFilterBar: 상태 타입 = ${state::class.simpleName}")
    
    if (state is SearchUiState.Success) {
        println("SearchResultFilterBar: Success 상태 - 검색어: '${state.searchQuery}', 카테고리: ${state.selectedCategory ?: "전체"}, 지역: ${state.selectedRegion ?: "전체"}")
    }
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // 왼쪽에 필터 칩들을 묶는 Row
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            if (state is SearchUiState.Success) {
                // 검색어 표시 (없으면 "모든 검색어"로 표시)
                FilterChip(
                    label = if (state.searchQuery.isNotEmpty()) state.searchQuery else "모든 검색어",
                    onClick = { }
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                // 선택된 카테고리 표시 (null이면 "전체 카테고리"로 표시)
                FilterChip(
                    label = state.selectedCategory ?: "전체 카테고리",
                    onClick = { }
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                // 선택된 지역 표시 (null이면 "전체 지역"으로 표시)
                FilterChip(
                    label = state.selectedRegion ?: "전체 지역",
                    onClick = { }
                )
            }
        }
            
        // 정렬 방식 필터 아이콘 (오른쪽 끝에 배치)
        IconButton(
            onClick = onFilterClick
        ) {
            Icon(
                imageVector = Icons.Default.FilterList,
                contentDescription = "정렬 필터",
                tint = Color.Gray
            )
        }
    }
}

/**
 * 검색 결과 아이템
 */
@Composable
fun SearchResultItem(
    result: SearchResult
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* 아이템 클릭 처리 */ }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 아이콘
        Icon(
            painter = painterResource(id = result.icon),
            contentDescription = null,
            modifier = Modifier.width(40.dp),
            tint = Color.Gray
        )
        
        // 상품 정보
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp)
        ) {
            Text(
                text = result.title,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = result.place,
                color = Color.Gray,
                fontSize = 14.sp
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = "${result.reward} P",
                color = Color(0xFF1785E4),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
        
        // 남은 시간
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFE3F2FD)
            ),
            shape = RoundedCornerShape(4.dp)
        ) {
            Text(
                text = "${result.remainingDays} days left",
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                color = Color(0xFF1785E4),
                fontSize = 12.sp
            )
        }
    }
}

/**
 * 정렬 옵션 아이템
 */
@Composable
fun SortOption(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .clickable(onClick = onClick)
            .background(if (isSelected) Color(0xFFE3F2FD) else Color.Transparent)
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = if (isSelected) Color(0xFF1785E4) else Color.Black,
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
        )
    }
} 