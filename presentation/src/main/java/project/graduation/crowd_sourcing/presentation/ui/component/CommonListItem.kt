package project.graduation.crowd_sourcing.presentation.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import project.graduation.crowd_sourcing.presentation.R

@Composable
fun CommonListItem(
    mainText: String,
    subText: String,
    leftText: String = "",
    @DrawableRes icon: Int,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clickable { }
            .padding(vertical = dimensionResource(R.dimen.space_small)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_list_box),
            contentDescription = null
        )

        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.space_small)))

        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            Text(
                text = mainText,
                style = TextStyle(fontSize = dimensionResource(id = R.dimen.sp_large).value.sp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = subText,
                color = colorResource(R.color.darker_gary),
                style = TextStyle(fontSize = dimensionResource(id = R.dimen.sp_small).value.sp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Text(
            text = leftText,
            style = TextStyle(fontSize = dimensionResource(id = R.dimen.sp_large).value.sp),
            fontWeight = FontWeight.Bold,
        )
    }
}

@Preview
@Composable
fun ListItemPrev() {
    CommonListItem(
        "main",
        "sub",
        leftText = "left",
        icon = R.drawable.ic_list_box,
        onClick = {}
    )
}