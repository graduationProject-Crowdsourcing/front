package project.graduation.crowd_sourcing.presentation.ui.screen.search.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 선택된 필터를 표시하는 섹션
 *
 * @param selectedCategory 선택된 카테고리
 * @param selectedRegion 선택된 지역
 * @param onFilterClick 필터 클릭 콜백
 * @param modifier 컴포넌트에 적용할 추가 수정자
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SelectedFiltersSection(
    selectedCategory: String?,
    selectedRegion: String?,
    onFilterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // 선택된 필터가 있는지 확인
    val hasSelectedFilters = selectedCategory != null || selectedRegion != null

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        // 필터링 안내 텍스트
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "필터링",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            // 선택된 필터가 있으면 뱃지 표시
            if (hasSelectedFilters) {
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF1785E4))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "필터 적용됨",
                        fontSize = 12.sp,
                        color = Color.White
                    )
                }
            }
        }
        
        // 통합 필터링 행 (카테고리 + 지역)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(4.dp))
                .background(if (hasSelectedFilters) Color(0xFFE3F2FD) else Color(0xFFF5F5F5))
                .clickable(onClick = onFilterClick)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 필터 정보 표시
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // 카테고리 정보
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "카테고리",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.weight(0.3f)
                    )
                    
                    Text(
                        text = selectedCategory ?: "전체",
                        fontSize = 14.sp,
                        color = if (selectedCategory != null) Color(0xFF1785E4) else Color.Black,
                        fontWeight = if (selectedCategory != null) FontWeight.Medium else FontWeight.Normal,
                        modifier = Modifier.weight(0.7f),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                // 지역 정보
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "지역",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.weight(0.3f)
                    )
                    
                    Text(
                        text = selectedRegion ?: "전체",
                        fontSize = 14.sp,
                        color = if (selectedRegion != null) Color(0xFF1785E4) else Color.Black,
                        fontWeight = if (selectedRegion != null) FontWeight.Medium else FontWeight.Normal,
                        modifier = Modifier.weight(0.7f),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }
            }
            
            Icon(
                imageVector = Icons.Filled.KeyboardArrowRight,
                contentDescription = "필터 선택",
                tint = Color.Gray
            )
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // 선택된 필터 태그들 (FlowRow로 자동 줄바꿈)
        if (hasSelectedFilters) {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // 선택된 카테고리가 있으면 표시
                if (selectedCategory != null) {
                    FilterChip(
                        label = "카테고리: $selectedCategory",
                        onClick = onFilterClick
                    )
                }
                
                // 선택된 지역이 있으면 표시
                if (selectedRegion != null) {
                    FilterChip(
                        label = "지역: $selectedRegion",
                        onClick = onFilterClick
                    )
                }
            }
        }
    }
}

/**
 * 필터 칩 컴포넌트
 *
 * @param label 라벨 텍스트
 * @param onClick 클릭 이벤트 콜백
 */
@Composable
fun FilterChip(
    label: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.clip(RoundedCornerShape(16.dp)),
        color = Color(0xFFE3F2FD)
    ) {
        Box(
            modifier = Modifier
                .clickable(onClick = onClick)
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(
                text = label,
                fontSize = 12.sp,
                color = Color(0xFF1785E4)
            )
        }
    }
} 