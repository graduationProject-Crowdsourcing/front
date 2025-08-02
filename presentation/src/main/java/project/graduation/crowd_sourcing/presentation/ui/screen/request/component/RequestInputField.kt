package project.graduation.crowd_sourcing.presentation.ui.screen.request.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.KeyboardType
import project.graduation.crowd_sourcing.presentation.ui.component.EditTextBox

// 의뢰 작성 페이지 - 아이콘 + 라벨 + 입력창 컴포넌트
@Composable
fun InputTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    @DrawableRes iconResId: Int,
    keyboardType: KeyboardType = KeyboardType.Text,
    modifier: Modifier = Modifier,
    customContent: (@Composable () -> Unit)? = null
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Box(modifier = Modifier.width(30.dp)) {
            // 아이콘
            Icon(
                painter = painterResource(id = iconResId),
                contentDescription = null,
                modifier = Modifier.padding(top = 25.dp, end = 15.dp)
            )
        }

        // 라벨 + 입력창 (세로 정렬)
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = label,
                fontSize = 14.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            customContent?.invoke() ?: EditTextBox(
                value = value,
                onValueChange = onValueChange,
                placeHolder = placeholder,
                keyboardType = keyboardType,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

}
