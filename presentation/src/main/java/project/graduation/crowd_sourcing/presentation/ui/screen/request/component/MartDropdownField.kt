package project.graduation.crowd_sourcing.presentation.ui.screen.request.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
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

@Composable
fun MartDropdownField(
    label: String,
    selectedMart: String,
    martList: List<String>,
    onMartSelected: (String) -> Unit,
    @DrawableRes iconResId: Int
) {
    var expanded by remember { mutableStateOf(false) }

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
            modifier = Modifier
                .padding(top = 20.dp, end = 15.dp)
        )

        // 라벨 + 드롭다운 필드
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = label,
                fontSize = 14.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Box {
                OutlinedTextField(
                    value = selectedMart,
                    onValueChange = {},
                    readOnly = true,
                    placeholder = { Text("마트 선택") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expanded = true },
                    trailingIcon = {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                    }
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    martList.forEach { mart ->
                        DropdownMenuItem(
                            text = { Text(mart) },
                            onClick = {
                                onMartSelected(mart)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}
