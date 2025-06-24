package project.graduation.crowd_sourcing.presentation.ui.screen.request.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// 작업 제출 페이지 - 작업 내용 컴포넌트
@Composable
fun WorkInfo(
    icon: ImageVector,
    label: String,
    value: String,
    verticalPadding: Dp = 8.dp, // 세로 패딩 커스터마이징 가능
    iconSize: Dp = 20.dp, // 아이콘 크기 커스터마이징 가능
    spacerWidth: Dp = 12.dp, // 간격 커스터마이징 가능
    labelWidth: Dp = 80.dp, // 라벨 너비 커스터마이징 가능
    labelStyle: TextStyle = MaterialTheme.typography.bodyMedium, // 라벨 스타일 커스터마이징 가능
    valueStyle: TextStyle = MaterialTheme.typography.bodyMedium // 값 스타일 커스터마이징 가능
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = verticalPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(iconSize)
        )
        Spacer(modifier = Modifier.width(spacerWidth))
        Text(
            text = label,
            style = labelStyle,
            modifier = Modifier.width(labelWidth)
        )
        Text(
            text = value,
            style = valueStyle,
            modifier = Modifier.weight(1f)
        )
    }
}
