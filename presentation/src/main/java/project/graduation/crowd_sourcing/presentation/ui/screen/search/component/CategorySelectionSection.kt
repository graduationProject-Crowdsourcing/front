package project.graduation.crowd_sourcing.presentation.ui.screen.search.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 카테고리 선택 섹션 컴포넌트
 *
 * @param categories 카테고리 목록
 * @param selectedCategory 현재 선택된 카테고리
 * @param onCategorySelected 카테고리 선택 시 호출할 콜백
 * @param modifier 컴포넌트에 적용할 추가 수정자
 */
@Composable
fun CategorySelectionSection(
    categories: List<String>,
    selectedCategory: String?,
    onCategorySelected: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        // 헤더 텍스트
        Text(
            text = "카테고리 선택",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        // 카테고리 선택 행
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 첫 3개 카테고리 표시
            categories.take(3).forEach { category ->
                val isSelected = (selectedCategory == category) || (selectedCategory == null && category == "전체")
                LocationChip(
                    text = category,
                    isSelected = isSelected,
                    onClick = { onCategorySelected(if (category == "전체") null else category) },
                    modifier = Modifier.weight(1f)
                )
            }
            
            // 더보기 아이콘
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "더 많은 카테고리",
                tint = Color.Black,
                modifier = Modifier.size(16.dp)
            )
        }
    }
} 