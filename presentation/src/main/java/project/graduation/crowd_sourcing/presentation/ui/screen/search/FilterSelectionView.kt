package project.graduation.crowd_sourcing.presentation.ui.screen.search

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import project.graduation.crowd_sourcing.presentation.R

/**
 * 필터 선택 화면 (카테고리 및 지역 선택)
 * 
 * @param navController 네비게이션 컨트롤러
 */
@Composable
fun FilterSelectionView(
    navController: NavController
) {
    val viewModel: SearchViewModel = hiltViewModel()
    val uiState = viewModel.uiState.collectAsState()
    
    // 이전 화면에서 전달받은 초기 카테고리/지역 정보
    val initialCategory = navController.previousBackStackEntry?.savedStateHandle?.get<String?>("initialCategory")
    val initialRegion = navController.previousBackStackEntry?.savedStateHandle?.get<String?>("initialRegion")
    
    // 디버깅 로그 및 초기 데이터 로딩
    LaunchedEffect(Unit) {
        println("FilterSelectionView: 초기값 받음 - 카테고리=${initialCategory ?: "전체"}, 지역=${initialRegion ?: "전체"}")
        
        // 초기 데이터 로딩 확인 및 로드
        viewModel.loadInitialData()
    }
    
    when (val state = uiState.value) {
        is SearchUiState.Loading -> {
            // 로딩 화면
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is SearchUiState.Error -> {
            // 에러 화면
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "데이터 로딩 중 오류가 발생했습니다: ${state.message}")
            }
        }
        is SearchUiState.Success -> {
            FilterSelectionContent(
                navController = navController,
                state = state,
                initialCategory = initialCategory,
                initialRegion = initialRegion
            )
        }
    }
}

@Composable
private fun FilterSelectionContent(
    navController: NavController,
    state: SearchUiState.Success,
    initialCategory: String?,
    initialRegion: String?
) {
    // 현재 선택된 카테고리와 지역
    val selectedCategory = remember { mutableStateOf(initialCategory ?: "전체") }
    val selectedRegion = remember { mutableStateOf(initialRegion ?: "전체") }
    
    // 전체 옵션이 명시적으로 선택되었는지 여부
    val isAllCategorySelected = remember { mutableStateOf(initialCategory == null || initialCategory == "전체") }
    val isAllRegionSelected = remember { mutableStateOf(initialRegion == null || initialRegion == "전체") }
    
    // 초기값 설정 (Success 상태가 된 후)
    LaunchedEffect(state) {
        // 전달받은 초기값이 있으면 적용
        if (initialCategory != null) {
            selectedCategory.value = initialCategory
            isAllCategorySelected.value = (initialCategory == "전체")
            println("FilterSelectionView: 카테고리 초기값 적용 - ${initialCategory}")
        }
        if (initialRegion != null) {
            selectedRegion.value = initialRegion
            isAllRegionSelected.value = (initialRegion == "전체")
            println("FilterSelectionView: 지역 초기값 적용 - ${initialRegion}")
        }
    }
    
    val scrollState = rememberScrollState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        // 지역 선택 섹션
        Text(
            text = "지역선택",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // 지역 선택 그리드 (2x2)
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp) // 고정 높이
        ) {
            // 전체 옵션
            item {
                RegionItem(
                    name = "전체",
                    isSelected = isAllRegionSelected.value,
                    onClick = { 
                        isAllRegionSelected.value = true
                        selectedRegion.value = "전체"
                        println("FilterSelectionView: 지역 '전체' 선택")
                    }
                )
            }
            
            // 나머지 지역 옵션들
            items(state.regions.filter { it != "전체" }) { region ->
                RegionItem(
                    name = region,
                    isSelected = selectedRegion.value == region && !isAllRegionSelected.value,
                    onClick = { 
                        isAllRegionSelected.value = false
                        selectedRegion.value = region
                        println("FilterSelectionView: 지역 '$region' 선택")
                    }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // 카테고리 선택 섹션
        Text(
            text = "카테고리 선택",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // 카테고리 선택 그리드 (2x2)
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(330.dp) // 고정 높이
        ) {
            // 전체 옵션
            item {
                CategoryItem(
                    name = "전체",
                    isSelected = isAllCategorySelected.value,
                    onClick = { 
                        isAllCategorySelected.value = true
                        selectedCategory.value = "전체"
                        println("FilterSelectionView: 카테고리 '전체' 선택")
                    }
                )
            }
            
            // 모든 카테고리 표시
            items(state.categories.filter { it != "전체" }) { category ->
                CategoryItem(
                    name = category,
                    isSelected = selectedCategory.value == category && !isAllCategorySelected.value,
                    onClick = { 
                        isAllCategorySelected.value = false
                        selectedCategory.value = category
                        println("FilterSelectionView: 카테고리 '$category' 선택")
                    }
                )
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        // 하단 버튼 영역
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // 취소 버튼
            Button(
                onClick = { 
                    navController.popBackStack()
                },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.LightGray
                )
            ) {
                Text(
                    text = "취소",
                    color = Color.Black
                )
            }
            
            // 적용 버튼
            Button(
                onClick = { 
                    // 선택된 값을 다음 화면으로 전달
                    navController.previousBackStackEntry?.savedStateHandle?.set(
                        "selectedCategory", selectedCategory.value
                    )
                    
                    navController.previousBackStackEntry?.savedStateHandle?.set(
                        "selectedRegion", selectedRegion.value
                    )
                    
                    // 디버깅용 로그
                    println("필터 선택 적용: 카테고리=${selectedCategory.value ?: "오류"}, 지역=${selectedRegion.value ?: "오류"}")
                    
                    // 이전 화면으로 돌아가기
                    navController.popBackStack()
                },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1785E4)
                )
            ) {
                Text(
                    text = "적용",
                    color = Color.White
                )
            }
        }
    }
}

/**
 * 지역 아이템 컴포넌트
 */
@Composable
fun RegionItem(
    name: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) Color(0xFFDDDDDD) else Color.White
    val icon = R.drawable.ic_list_box
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(backgroundColor)
            .border(
                width = 1.dp,
                color = Color.LightGray,
                shape = RoundedCornerShape(4.dp)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = name,
                tint = Color.Gray,
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Text(
                text = name,
                fontSize = 14.sp,
                color = Color.Black,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}

/**
 * 카테고리 아이템 컴포넌트
 */
@Composable
fun CategoryItem(
    name: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) Color(0xFFDDDDDD) else Color.White
    val icon = getCategoryIcon(name)
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(backgroundColor)
            .border(
                width = 1.dp,
                color = Color.LightGray,
                shape = RoundedCornerShape(4.dp)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = name,
                tint = Color.Gray,
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Text(
                text = name,
                fontSize = 14.sp,
                color = Color.Black,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}

/**
 * 카테고리별 아이콘 리소스 ID 반환
 */
fun getCategoryIcon(category: String): Int {
    return when (category) {
        "과자/스낵" -> R.drawable.ic_list_box
        "라면/면류" -> R.drawable.ic_list_box
        "통조림/캔" -> R.drawable.ic_list_box
        "유제품" -> R.drawable.ic_list_box
        "냉동식품" -> R.drawable.ic_list_box
        "즉석식품" -> R.drawable.ic_list_box
        "소스/양념" -> R.drawable.ic_list_box
        "음료/커피" -> R.drawable.ic_list_box
        "쌀/잡곡" -> R.drawable.ic_list_box
        else -> R.drawable.ic_list_box
    }
}

@Preview
@Composable
fun FilterSelectionViewPrev(){
    FilterSelectionView(navController = rememberNavController())
}