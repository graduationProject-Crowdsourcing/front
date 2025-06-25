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
import project.graduation.crowd_sourcing.presentation.ui.component.EditTextBox

@Composable
fun DistrictSearchField(
    query: String,
    suggestions: List<String>,
    onQueryChange: (String) -> Unit,
    onSuggestionClick: (String) -> Unit,
    @DrawableRes iconResId: Int
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
                    text = "지역 검색",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                EditTextBox(
                    value = query,
                    onValueChange = onQueryChange,
                    placeHolder = "지역구 이름을 입력하세요"
                )
            }
        }

        if (suggestions.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 40.dp)
            ) {
                suggestions.forEach { district ->
                    Text(
                        text = district,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSuggestionClick(district) }
                            .padding(vertical = 8.dp, horizontal = 4.dp),
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
                }
            }
        }
    }
}
