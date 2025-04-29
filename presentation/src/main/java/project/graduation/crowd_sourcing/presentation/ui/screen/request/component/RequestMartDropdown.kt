package project.graduation.crowd_sourcing.presentation.ui.screen.request.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import project.graduation.crowd_sourcing.presentation.ui.component.EditTextBox
import project.graduation.crowd_sourcing.presentation.ui.screen.request.request.MartInfo

// 의뢰 작성 페이지 - 마트 선택 드롭다운 컴포넌트
@Composable
fun MartDropdownField(
    label: String,                       // 드롭다운 라벨 (ex. "마트 선택")
    selectedMart: String,                // 현재 선택된 마트 이름
    martList: List<MartInfo>,            // 드롭다운에 표시할 마트 리스트
    onMartSelected: (MartInfo) -> Unit,    // 마트 선택 시 호출할 콜백
    @DrawableRes iconResId: Int          // 아이콘 리소스 ID
) {
    var expanded by remember { mutableStateOf(false) } // 드롭다운 열림 여부 상태

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        // 좌측 아이콘
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = null,
            modifier = Modifier
                .padding(top = 25.dp, end = 15.dp)
        )

        // 라벨 + 드롭다운 필드
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = label,
                fontSize = 14.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            // 드롭다운 전체 컨테이너
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.TopStart)
            ) {
                // 선택된 마트 보여주는 필드
                EditTextBox(
                    value = selectedMart,
                    onValueChange = {}, // 입력은 막기
                    placeHolder = "마트 선택",
                    readOnly = true,
                    trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null) },
                    onClick = { expanded = true }
                )

                // 실제 드롭다운 메뉴
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    martList.forEach { mart ->
                        DropdownMenuItem(
                            text = { Text(mart.name) },
                            onClick = {
                                onMartSelected(mart) // 전체 객체 전달
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}
