package project.graduation.crowd_sourcing.presentation.ui.component.list

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import project.graduation.crowd_sourcing.presentation.R
import project.graduation.crowd_sourcing.presentation.ui.component.GrayDivider

@Composable
fun CommonList(
    list: List<CommonListItemData>
) {
    LazyColumn {
        itemsIndexed(list) { index, item ->
            CommonListItem(
                CommonListItemData(
                    leftText = item.leftText,
                    mainText = item.mainText,
                    subText = item.subText,
                    onClick = item.onClick
                )
            )
            if (index < list.size - 1) {
                GrayDivider(Modifier.padding(horizontal = dimensionResource(R.dimen.space_small)))
            }
        }
    }
}
