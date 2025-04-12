package project.graduation.crowd_sourcing.presentation.ui.screen.search.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 검색 버튼 섹션
 *
 * @param onSearchClick 검색 버튼 클릭 콜백
 * @param modifier 컴포넌트에 적용할 추가 수정자
 */
@Composable
fun SearchButtonSection(
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // 버튼에 사용할 메인 색상
    val mainColor = Color(0xFF1785E4)
    val lightBlue = Color(0xFFE3F2FD)
    
    // 버튼 상호작용 상태 관리
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isPressed by interactionSource.collectIsPressedAsState()
    
    // hover 또는 press 상태에 따라 배경색 변경
    val backgroundColor = when {
        isPressed -> lightBlue.copy(alpha = 0.7f)
        isHovered -> lightBlue.copy(alpha = 0.3f)
        else -> Color.Transparent
    }
    
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        // 검색 버튼 - 상호작용에 따른 시각적 피드백 추가
        Button(
            onClick = onSearchClick,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = backgroundColor,  // 상태에 따른 배경색
                contentColor = mainColor  // 텍스트 색상
            ),
            border = BorderStroke(1.dp, mainColor),  // 테두리 설정
            contentPadding = PaddingValues(vertical = 14.dp),  // 상하 패딩 추가
            interactionSource = interactionSource,  // 상호작용 소스 설정
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 0.dp,
                pressedElevation = 0.dp,
                hoveredElevation = 2.dp  // hover 시 약간 올라오는 효과
            )
        ) {
            Text(
                text = "검색하기",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,  // 텍스트를 약간 더 두껍게
                color = mainColor
            )
        }
    }
} 