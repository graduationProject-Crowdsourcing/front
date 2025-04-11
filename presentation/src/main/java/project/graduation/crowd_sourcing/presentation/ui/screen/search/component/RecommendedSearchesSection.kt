package project.graduation.crowd_sourcing.presentation.ui.screen.search.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import project.graduation.crowd_sourcing.presentation.ui.component.GrayDivider

@Composable
fun RecommendedSearchesSection(
    recommendedSearches: List<String>,
    onSearchTermClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (recommendedSearches.isEmpty()) return

    Column(modifier = modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "추천 검색",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Column {
            // 최대 3개의 추천 검색어만 표시
            recommendedSearches.take(3).forEach { searchTerm ->
                RecommendedSearchItem(
                    searchTerm = searchTerm,
                    onClick = { onSearchTermClick(searchTerm) }
                )
                GrayDivider()
            }
        }
    }
}

@Composable
private fun RecommendedSearchItem(
    searchTerm: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search Icon",
            tint = Color.Gray,
            modifier = Modifier.size(20.dp) // 아이콘 크기 조정
        )
        Spacer(modifier = Modifier.width(12.dp)) // 아이콘과 텍스트 사이 간격 조정
        Text(
            text = searchTerm,
            fontSize = 14.sp,
            color = Color.DarkGray
        )
    }
} 