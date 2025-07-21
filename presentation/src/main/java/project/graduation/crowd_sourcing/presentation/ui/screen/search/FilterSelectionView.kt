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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
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
                CircularProgressIndicator(
                    color = Color(0xFF1785E4)
                )
            }
        }
        is SearchUiState.Error -> {
            // 에러 화면
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "데이터 로딩 중 오류가 발생했습니다: ${state.message}",
                    color = Color.Red
                )
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
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        // 컨텐츠 영역
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .padding(bottom = 80.dp) // 하단 버튼 공간 확보
        ) {
            // 지역 선택 섹션
            Text(
                text = "🗺️ 지역 선택",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2C3E50),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            // 지역 선택 그리드 (2x2)
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp) // 고정 높이
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
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // 카테고리 선택 섹션
            Text(
                text = "🏷️ 카테고리 선택",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2C3E50),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            // 카테고리 선택 그리드 (2x2)
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp) // 고정 높이
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
            
            Spacer(modifier = Modifier.height(24.dp))
        }
        
        // 하단 고정 버튼 영역
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color(0xFFF8F9FA).copy(alpha = 0.9f),
                            Color(0xFFF8F9FA)
                        )
                    )
                )
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // 취소 버튼
                Button(
                    onClick = { 
                        navController.popBackStack()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                        .shadow(
                            elevation = 2.dp,
                            shape = RoundedCornerShape(12.dp)
                        ),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 4.dp
                    )
                ) {
                    Text(
                        text = "취소",
                        color = Color(0xFF6C757D),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
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
                        .height(56.dp)
                        .shadow(
                            elevation = 4.dp,
                            shape = RoundedCornerShape(12.dp)
                        ),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1785E4)
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 6.dp
                    )
                ) {
                    Text(
                        text = "적용",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

/**
 * 지역 아이템 컴포넌트 - 개선된 디자인
 */
@Composable
fun RegionItem(
    name: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val cardColors = if (isSelected) {
        CardDefaults.cardColors(
            containerColor = Color(0xFF1785E4)
        )
    } else {
        CardDefaults.cardColors(
            containerColor = Color.White
        )
    }
    
    val textColor = if (isSelected) Color.White else Color(0xFF2C3E50)
    val iconTint = if (isSelected) Color.White else Color(0xFF6C757D)
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = cardColors,
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 8.dp else 4.dp,
            pressedElevation = 12.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_place),
                contentDescription = name,
                tint = iconTint,
                modifier = Modifier.size(28.dp)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = name,
                fontSize = 14.sp,
                color = textColor,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                maxLines = 1
            )
        }
    }
}

/**
 * 카테고리 아이템 컴포넌트 - 개선된 디자인
 */
@Composable
fun CategoryItem(
    name: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val cardColors = if (isSelected) {
        CardDefaults.cardColors(
            containerColor = Color(0xFF1785E4)
        )
    } else {
        CardDefaults.cardColors(
            containerColor = Color.White
        )
    }
    
    val textColor = if (isSelected) Color.White else Color(0xFF2C3E50)
    val iconTint = if (isSelected) Color.White else Color(0xFF6C757D)
    val icon = getCategoryIcon(name)
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = cardColors,
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 8.dp else 4.dp,
            pressedElevation = 12.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = name,
                tint = iconTint,
                modifier = Modifier.size(28.dp)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = name,
                fontSize = 12.sp,
                color = textColor,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                maxLines = 2,
                lineHeight = 14.sp
            )
        }
    }
}

/**
 * 카테고리별 아이콘 리소스 ID 반환 - 개선된 아이콘 매핑
 */
fun getCategoryIcon(category: String): Int {
    return when (category) {
        "전체" -> R.drawable.ic_list_box
        "과자/스낵" -> R.drawable.ic_star
        "라면/면류" -> R.drawable.ic_item
        "통조림/캔" -> R.drawable.ic_home
        "유제품" -> R.drawable.ic_point
        "냉동식품" -> R.drawable.ic_bell
        "즉석식품" -> R.drawable.ic_calendar
        "소스/양념" -> R.drawable.ic_support
        "음료/커피" -> R.drawable.ic_search
        "쌀/잡곡" -> R.drawable.ic_mart
        else -> R.drawable.ic_item
    }
}

@Preview
@Composable
fun FilterSelectionViewPrev(){
    FilterSelectionView(navController = rememberNavController())
}