package project.graduation.crowd_sourcing.presentation.ui.screen.request.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import project.graduation.crowd_sourcing.presentation.R
import project.graduation.crowd_sourcing.presentation.ui.component.EditTextBox

@Composable
fun DistrictSearchField(
    selectedRegions: List<String>,
    @DrawableRes iconResId: Int,
    onClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            Icon(
                painter = painterResource(id = iconResId),
                contentDescription = null,
                modifier = Modifier.padding(top = 25.dp, end = 15.dp)
            )
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "지역 선택",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onClick() }
                        .border(
                            width = 1.dp,
                            color = colorResource(id = R.color.gray),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .background(color = colorResource(id = R.color.white))
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    val label = if (selectedRegions.isNotEmpty()) {
                        selectedRegions.joinToString(", ")
                    } else {
                        "지역구를 선택해주세요"
                    }

                    Text(
                        text = label,
                        color = if (selectedRegions.isNotEmpty()) Color.Black else Color.Gray,
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

