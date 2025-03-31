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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import project.graduation.crowd_sourcing.presentation.R
import project.graduation.crowd_sourcing.presentation.utils.spaceSmall
import project.graduation.crowd_sourcing.presentation.utils.textStyleLarge
import project.graduation.crowd_sourcing.presentation.utils.textStyleSmall

@Composable
fun CommonListItem(
    mainText: String,
    subText: String,
    @DrawableRes icon: Int,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clickable {  }
            .padding(vertical = spaceSmall())
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_list_box),
            contentDescription = null
        )

        Spacer(modifier = Modifier.width(spaceSmall()))

        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            Text(
                text = mainText,
                style = textStyleLarge(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = subText,
                color = colorResource(R.color.darker_gary),
                style = textStyleSmall(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview
@Composable
fun ListItemPrev(){
    CommonListItem(
        "main",
        "sub",
        R.drawable.ic_list_box,
        {}
    )
}