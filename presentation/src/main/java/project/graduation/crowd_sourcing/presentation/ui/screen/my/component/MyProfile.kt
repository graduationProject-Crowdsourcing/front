package project.graduation.crowd_sourcing.presentation.ui.screen.my.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import project.graduation.crowd_sourcing.presentation.R
import project.graduation.crowd_sourcing.presentation.ui.screen.my.MyUiState
import project.graduation.crowd_sourcing.presentation.utils.textStyleLarge
import project.graduation.crowd_sourcing.presentation.utils.textStyleSmall

@Composable
fun MyProfile(myUiState: MyUiState) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = dimensionResource(id = R.dimen.space_medium)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.ic_my),
            contentDescription = "profile image",
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .border(1.dp, colorResource(R.color.gray), CircleShape),
            alignment = Alignment.Center
        )

        Column(
            modifier = Modifier
                .padding(horizontal = dimensionResource(R.dimen.space_small))
                .weight(1f)
        ) {
            Text(
                text = myUiState.nickname,
                style = textStyleLarge(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "Points: ${myUiState.point}",
                color = colorResource(R.color.darker_gary),
                style = textStyleSmall(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Box(
            modifier = Modifier
                .width(100.dp)
                .height(32.dp)
                .clickable { }
                .clip(RoundedCornerShape(8.dp))
                .background(colorResource(R.color.gray))
                .weight(1f)
        ) {
            Text(
                text = "프로필 수정",
                style = textStyleSmall(),
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Preview
@Composable
fun MyProfilePrev() {
    MyProfile(MyUiState.init())
}