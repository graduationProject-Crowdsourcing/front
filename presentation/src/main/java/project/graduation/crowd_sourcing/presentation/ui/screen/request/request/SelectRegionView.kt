package project.graduation.crowd_sourcing.presentation.ui.screen.request.request

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import project.graduation.crowd_sourcing.domain.model.entity.martsearch.MartEntity
import project.graduation.crowd_sourcing.presentation.R
import project.graduation.crowd_sourcing.presentation.ui.component.ConfirmButton

@Composable
fun SelectRegionView(
    navController: NavController,
    viewModel: SelectRegionViewModel = hiltViewModel()
) {
    val regionList = listOf(
        "강남구", "강동구", "강북구", "강서구", "관악구",
        "광진구", "구로구", "금천구", "노원구", "도봉구",
        "동대문구", "동작구", "마포구", "서대문구", "서초구",
        "성동구", "성북구", "송파구", "양천구", "영등포구",
        "용산구", "은평구", "종로구", "중구", "중랑구"
    )

    val selectedRegions by viewModel.selectedRegions.collectAsState()
    val selectedMarts by viewModel.selectedMarts.collectAsState()
    val martList by viewModel.martList.collectAsState()

    val prefillRegions = navController.previousBackStackEntry
        ?.savedStateHandle?.get<List<String>>("selectedRegions")
    val prefillMarts = navController.previousBackStackEntry
        ?.savedStateHandle?.get<List<MartEntity>>("selectedMarts")

    androidx.compose.runtime.LaunchedEffect(Unit) {
        if (!prefillRegions.isNullOrEmpty() || !prefillMarts.isNullOrEmpty()) {
            viewModel.prefillSelections(
                regions = prefillRegions ?: emptyList(),
                marts = prefillMarts?.map { it.martName } ?: emptyList()
            )
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
            
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(regionList) { region ->
                    RegionSelectionItem(
                        name = region,
                        isSelected = region in selectedRegions,
                        onClick = { viewModel.onRegionToggle(region) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 마트 선택 섹션
            Text(
                text = "🏪 마트 선택",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2C3E50),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(martList) { mart ->
                    MartSelectionItem(
                        name = mart.martName,
                        isSelected = selectedMarts.contains(mart.martName),
                        onClick = { viewModel.onMartToggle(mart.martName) }
                    )
                }
            }
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
            Button(
                onClick = {
                    val selectedRegion = selectedRegions.firstOrNull()
                    if (selectedRegion != null) {
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("selectedRegion", selectedRegion)
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("selectedMarts_prefill", selectedMarts)
                    }
                    navController.popBackStack()
                },
                enabled = selectedRegions.isNotEmpty() && selectedMarts.isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .shadow(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(12.dp)
                    ),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1785E4),
                    disabledContainerColor = Color(0xFFE0E0E0)
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 6.dp
                )
            ) {
                Text(
                    text = "선택 완료",
                    color = if (selectedRegions.isNotEmpty() && selectedMarts.isNotEmpty()) Color.White else Color(0xFF9E9E9E),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

/**
 * 지역 선택 아이템 컴포넌트 - 개선된 디자인
 */
@Composable
fun RegionSelectionItem(
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
                maxLines = 1,
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * 마트 선택 아이템 컴포넌트 - 개선된 디자인
 */
@Composable
fun MartSelectionItem(
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
                painter = painterResource(id = R.drawable.ic_mart),
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
                textAlign = TextAlign.Center,
                lineHeight = 14.sp
            )
        }
    }
}
