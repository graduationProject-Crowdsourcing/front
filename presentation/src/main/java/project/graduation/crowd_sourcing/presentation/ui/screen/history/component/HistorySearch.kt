package project.graduation.crowd_sourcing.presentation.ui.screen.history.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.wear.compose.material.ChipDefaults
import project.graduation.crowd_sourcing.presentation.R
import project.graduation.crowd_sourcing.presentation.ui.navigation.Screen

@Composable
fun HistorySearch(
    categories: List<String>,
    selectedCategory: List<String>,
    onCategorySelected: (String) -> Unit,
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onSearchClicked: () -> Unit,
    modifier: Modifier = Modifier,
    navController: NavController
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row {
            LazyRow(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.space_small))
            ) {
                items(categories) { category ->
                    FilterChip(
                        selected = false,
                        onClick = { onCategorySelected(category) },
                        label = { Text(text = category) },
                        colors = FilterChipDefaults.filterChipColors().copy(
                            containerColor = if (selectedCategory.contains(category)) colorResource(
                                R.color.black50
                            ) else colorResource(
                                R.color.black5
                            ),
                        ),
                        border = BorderStroke(0.dp, Color.Transparent)
                    )
                }
            }

            IconButton(onClick = { navController.navigate(Screen.FilterSelectionScreen.route) }) {
                Icon(
                    painter = painterResource(id = R.drawable.img_filter),
                    contentDescription = "필터",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(RoundedCornerShape(dimensionResource(R.dimen.round_common)))
                .background(Color.White)
                .border(
                    1.dp,
                    colorResource(R.color.gray),
                    RoundedCornerShape(dimensionResource(R.dimen.round_common))
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextField(
                value = searchQuery,
                onValueChange = { onSearchQueryChanged(it) },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    unfocusedIndicatorColor = colorResource(R.color.gray),
                    focusedIndicatorColor = colorResource(R.color.gray)
                ),
                textStyle = TextStyle(fontSize = dimensionResource(id = R.dimen.sp_medium).value.sp),
                singleLine = true,
                modifier = Modifier.weight(1f)
            )

            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .background(
                        color = colorResource(R.color.primary),
                        shape = RoundedCornerShape(dimensionResource(R.dimen.round_common))
                    )
                    .height(32.dp)
                    .width(40.dp)
                    .clickable { onSearchClicked },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_search),
                    contentDescription = "search_button",
                    tint = Color.White,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HistorySearchPrev() {
    HistorySearch(
        categories = listOf("전체", "가공식품", "생활용품", "의류", "전자제품"),
        selectedCategory = listOf("전체"),
        onCategorySelected = {},
        searchQuery = "",
        onSearchQueryChanged = {},
        onSearchClicked = {},
        navController = rememberNavController()
    )
}
