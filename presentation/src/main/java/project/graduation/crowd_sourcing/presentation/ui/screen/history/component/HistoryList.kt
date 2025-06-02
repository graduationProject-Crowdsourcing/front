package project.graduation.crowd_sourcing.presentation.ui.screen.history.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import project.graduation.crowd_sourcing.presentation.R
import project.graduation.crowd_sourcing.presentation.ui.component.list.CommonListItem
import project.graduation.crowd_sourcing.presentation.ui.component.GrayDivider
import project.graduation.crowd_sourcing.presentation.ui.component.list.CommonListItemData
import project.graduation.crowd_sourcing.presentation.ui.screen.history.HistoryUiState
import project.graduation.crowd_sourcing.presentation.utils.getTimeAgo

@Composable
fun HistoryList(
    listTitle: String,
    historyList: List<HistoryUiState.HistoryItem>,
    modifier: Modifier = Modifier,
    onClick: (String, String, Int) -> Unit
) {
    if(historyList.isNotEmpty()) {
        Column(
            modifier = modifier
        ) {
            Text(
                text = listTitle,
                style = TextStyle(fontSize = dimensionResource(id = R.dimen.sp_large).value.sp)
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.space_small)))

            historyList.forEach { historyItem ->
                historyItem.run {
                    CommonListItem(
                        CommonListItemData(
                            icon = R.drawable.ic_list_box,
                            mainText = product,
                            subText = "${getTimeAgo(date)} / ${category}",
                            leftText = "${point} points",
                            onClick = {
                                historyItem.run { onClick(product, category, id) }
                            }
                        )
                    )
                }
                GrayDivider()
            }
        }
    }
}

@Preview
@Composable
fun HistoryListPrev() {
//    HistoryList("title", emptyList())
}