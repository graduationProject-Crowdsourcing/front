package project.graduation.crowd_sourcing.presentation.ui.screen.request.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import project.graduation.crowd_sourcing.presentation.ui.screen.request.work.Work

// 작업 리스트 페이지 - 리스트 아이템 컴포넌트
@Composable
fun WorkListItem(work: Work, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(Color(0xFFF0F0F0), shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Place,
                contentDescription = "위치",
                tint = Color(0xFF4285F4)
            )
        }
        Column(modifier = Modifier.weight(1f).padding(start = 16.dp)) {
            Text(text = work.title, style = MaterialTheme.typography.titleSmall)
            Text(text = work.place.koreanName, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }
        Text(
            text = "리워드 : ${work.reward}p",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
