package project.graduation.crowd_sourcing.presentation.ui.screen.search.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import project.graduation.crowd_sourcing.presentation.R

/**
 * 위치 또는 카테고리 선택을 위한 칩 컴포넌트
 *
 * @param text 칩에 표시할 텍스트
 * @param isSelected 현재 선택 상태
 * @param onClick 클릭 시 호출할 콜백
 * @param modifier 컴포넌트에 적용할 추가 수정자
 */
@Composable
fun LocationChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .padding(end = 8.dp)
            .clip(RoundedCornerShape(4.dp))
            .clickable { onClick() }
            .border(
                width = 1.dp,
                color = Color.Gray.copy(alpha = 0.3f),
                shape = RoundedCornerShape(4.dp)
            ),
        color = if (isSelected) Color.LightGray.copy(alpha = 0.7f) else Color.White,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = text,
                fontSize = 14.sp,
                color = Color.Black,
                fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
            )
        }
    }
}

@Preview
@Composable
fun LocationClipPrev(){
    LocationChip(
        text = "region",
        isSelected = true,
        onClick = {  },
        modifier = Modifier
    )
}