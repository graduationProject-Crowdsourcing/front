package project.graduation.crowd_sourcing.presentation.ui.screen.search.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import project.graduation.crowd_sourcing.presentation.R
import project.graduation.crowd_sourcing.presentation.ui.component.CommonListItem
import project.graduation.crowd_sourcing.presentation.ui.component.GrayDivider
import project.graduation.crowd_sourcing.presentation.ui.screen.search.SearchResult

/**
 * 검색 결과 섹션 컴포넌트
 *
 * @param title 섹션 제목
 * @param searchResults 검색 결과 목록
 * @param onItemClick 항목 클릭 시 호출할 콜백
 * @param modifier 컴포넌트에 적용할 추가 수정자
 */
@Composable
fun SearchResultSection(
    title: String,
    searchResults: List<SearchResult>,
    onItemClick: (SearchResult) -> Unit,
    modifier: Modifier = Modifier
) {
    if (searchResults.isEmpty()) return
    
    Surface(
        modifier = modifier,
        color = Color.White
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            // 섹션 제목
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp, top = 8.dp)
            )
            
            // 검색 결과 목록
            Column {
                searchResults.forEach { result ->
                    SearchResultItem(
                        searchResult = result,
                        onClick = { onItemClick(result) }
                    )
                    GrayDivider()
                }
            }
        }
    }
}

/**
 * 검색 결과 아이템 컴포넌트 - CommonListItem 공용 컴포넌트 사용
 *
 * @param searchResult 검색 결과 데이터
 * @param onClick 클릭 시 호출할 콜백
 */
@Composable
private fun SearchResultItem(
    searchResult: SearchResult,
    onClick: () -> Unit
) {
    CommonListItem(
        mainText = searchResult.title,
        subText = searchResult.place,
        leftText = "",
        icon = R.drawable.ic_list_box,  // 위치 아이콘이 필요하면 적절한 아이콘으로 교체
        onClick = onClick
    )
} 