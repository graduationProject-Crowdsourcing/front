package project.graduation.crowd_sourcing.presentation.ui.screen.my

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import project.graduation.crowd_sourcing.presentation.R

@Composable
fun MyView() {
    val viewModel: MyViewModel = hiltViewModel()
    val navController = rememberNavController()


    val textStyleLarge = TextStyle(fontSize = dimensionResource(id = R.dimen.sp_large).value.sp)
    val textStyleMedium = TextStyle(fontSize = dimensionResource(id = R.dimen.sp_medium).value.sp)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.space_medium))
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
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

            Column(modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.space_small))) {
                Text(
                    text = "user name",
                    style = textStyleLarge
                )
                Text(
                    text = "Points: nnn",
                    color = colorResource(R.color.darker_gary),
                    style = textStyleMedium
                )
            }

            Box(
                modifier = Modifier
                    .width(100.dp)
                    .height(32.dp)
                    .clickable { }
                    .background(colorResource(R.color.gray))
                    .clip(RoundedCornerShape(8.dp))
            ) {
                Text(
                    text = "프로필 수정",
                    style = textStyleMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Preview
@Composable
fun MyViewPrev() {
    MyView()
}