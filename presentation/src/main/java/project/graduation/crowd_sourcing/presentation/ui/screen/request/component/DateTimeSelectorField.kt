package project.graduation.crowd_sourcing.presentation.ui.screen.request.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color

@Composable
fun DateTimeSelectorField(
    label: String,
    dateTimeText: String,
    onClick: () -> Unit,
    @DrawableRes iconResId: Int
) {
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
            modifier = Modifier.padding(top = 20.dp, end = 15.dp)
        )

        // 라벨 + 날짜/시간 필드
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = label,
                fontSize = 14.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            OutlinedTextField(
                value = dateTimeText,
                onValueChange = {},
                readOnly = true,
                placeholder = { Text("의뢰 마감 날짜 및 시간 선택") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onClick() }
            )
        }
    }
}
