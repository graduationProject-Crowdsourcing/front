package project.graduation.crowd_sourcing.presentation.ui.screen.request.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import project.graduation.crowd_sourcing.presentation.ui.component.EditTextBox
import project.graduation.crowd_sourcing.presentation.ui.screen.request.request.MartInfo

@Composable
fun MartSearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    isSearching: Boolean,
    searchResults: List<MartInfo>,
    onMartSelected: (MartInfo) -> Unit,
    @DrawableRes iconResId: Int
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // 검색 필드
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            // 아이콘
            Icon(
                painter = painterResource(id = iconResId),
                contentDescription = null,
                modifier = Modifier.padding(top = 25.dp, end = 15.dp)
            )

            // 라벨과 검색 필드
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "지역 검색",
                    fontSize = 14.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                EditTextBox(
                    value = query,
                    onValueChange = onQueryChange,
                    placeHolder = "마트 이름을 입력하세요"
                )
            }
        }

        // 검색 결과
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 40.dp) // 아이콘 너비만큼 패딩
        ) {
            if (isSearching) {
                // 로딩 인디케이터
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .align(Alignment.Center),
                    strokeWidth = 2.dp,
                    color = Color.Gray
                )
            } else if (searchResults.isNotEmpty()) {
                // 검색 결과 목록을 Box로 감싸고 높이 제한 설정
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp) // 높이 제한 설정
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp)
                    ) {
                        items(searchResults) { mart ->
                            MartSearchResultItem(
                                mart = mart,
                                onMartSelected = onMartSelected
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MartSearchResultItem(
    mart: MartInfo,
    onMartSelected: (MartInfo) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onMartSelected(mart) }
            .padding(vertical = 8.dp, horizontal = 4.dp)
    ) {
        Text(
            text = mart.name,
            fontSize = 16.sp,
            color = Color.Black,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        
        Divider(
            modifier = Modifier.padding(top = 8.dp),
            color = Color.LightGray,
            thickness = 0.5.dp
        )
    }
} 